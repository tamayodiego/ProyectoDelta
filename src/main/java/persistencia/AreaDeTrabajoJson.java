package persistencia;

import Objetos.DeltaMatroide;
import Recursos.DatosSerializados;
import Recursos.ElementoFolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Guarda y carga el área de trabajo (árbol de carpetas con sus
 * delta-matroides) en formato JSON.
 *
 * Reemplaza a la serialización nativa de Java (data.00 / .dma): el formato
 * es legible, está versionado y no ejecuta código al leer archivos ajenos.
 *
 * La huella y la tabla de frecuencias no se guardan: se recalculan a partir
 * de la familia al cargar, igual que hace DeltaMatroide al construirse.
 *
 * Los primeros archivos de la versión 1 incluían un objeto "banderas" con
 * los modos de línea de comandos; ya no se escribe y al cargar se ignora.
 */
public final class AreaDeTrabajoJson {

    /** Versión del formato. Súbela si cambia la estructura del JSON. */
    public static final int VERSION = 1;

    // Sin escape HTML, para que nombres como "Twst's" se lean tal cual.
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    private AreaDeTrabajoJson() {
    }

    /** Error al leer un archivo que no tiene el formato esperado. */
    public static class FormatoInvalidoException extends Exception {
        public FormatoInvalidoException(String mensaje, Throwable causa) {
            super(mensaje, causa);
        }
    }

    // ------------------------------------------------------------ guardar

    /**
     * Escribe primero en un archivo temporal y después lo renombra, para
     * que una falla a medio guardar no deje el archivo anterior corrupto.
     */
    public static void guardar(DatosSerializados datos, Path ruta) throws IOException {
        ArchivoDto archivo = new ArchivoDto();
        archivo.version = VERSION;
        archivo.raiz = aDto(datos.getMatroides());

        Path carpeta = ruta.toAbsolutePath().getParent();
        Path temporal = Files.createTempFile(carpeta, ruta.getFileName().toString(), ".tmp");
        try {
            try (Writer w = Files.newBufferedWriter(temporal, StandardCharsets.UTF_8)) {
                GSON.toJson(archivo, w);
            }
            try {
                Files.move(temporal, ruta, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporal, ruta, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporal);
        }
    }

    // -------------------------------------------------------------- cargar

    public static DatosSerializados cargar(Path ruta) throws IOException, FormatoInvalidoException {
        ArchivoDto archivo;
        try (Reader r = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {
            archivo = GSON.fromJson(r, ArchivoDto.class);
        } catch (JsonParseException e) {
            throw new FormatoInvalidoException(ruta.getFileName() + " no es un JSON válido", e);
        }

        if (archivo == null || archivo.raiz == null) {
            throw new FormatoInvalidoException(ruta.getFileName() + " está vacío o incompleto", null);
        }
        if (archivo.version > VERSION) {
            throw new FormatoInvalidoException(ruta.getFileName() + " usa la versión "
                    + archivo.version + " del formato; esta app solo entiende hasta la "
                    + VERSION, null);
        }

        try {
            DatosSerializados datos = new DatosSerializados();
            datos.setMatroides(desdeDto(archivo.raiz, null));
            return datos;
        } catch (RuntimeException e) {
            throw new FormatoInvalidoException(ruta.getFileName() + " tiene datos inconsistentes: "
                    + e.getMessage(), e);
        }
    }

    // ------------------------------------------------- conversión a/de DTO

    private static NodoDto aDto(ElementoFolder elemento) {
        NodoDto nodo = new NodoDto();
        nodo.nombre = elemento.getNombre();
        nodo.carpeta = elemento.isIsFolder();
        nodo.expandido = elemento.expandContra;
        if (nodo.carpeta) {
            nodo.hijos = new ArrayList<>();
            for (ElementoFolder hijo : elemento.getFolder()) {
                nodo.hijos.add(aDto(hijo));
            }
        } else if (elemento.getMatrode() != null) {
            nodo.deltaMatroide = aDto(elemento.getMatrode());
        }
        return nodo;
    }

    private static DeltaMatroideDto aDto(DeltaMatroide dm) {
        DeltaMatroideDto dto = new DeltaMatroideDto();
        dto.nombre = dm.getNombreFam();
        dto.etiquetas = dm.getEtiquetas();
        dto.campo = dm.getCampoDeOperacion();
        dto.modoFactibles = dm.getModoFactibles();
        dto.ordenar = dm.isOrdenar();
        dto.matriz = dm.getM();
        dto.familia = new ArrayList<>();
        for (LinkedList<Integer> factible : dm.getFamilia()) {
            dto.familia.add(new ArrayList<>(factible));
        }
        return dto;
    }

    private static ElementoFolder desdeDto(NodoDto nodo, ElementoFolder padre) {
        if (nodo.carpeta) {
            LinkedList<ElementoFolder> hijos = new LinkedList<>();
            ElementoFolder carpeta = new ElementoFolder(true, nodo.nombre, hijos, padre);
            carpeta.expandContra = nodo.expandido;
            if (nodo.hijos != null) {
                for (NodoDto hijo : nodo.hijos) {
                    hijos.add(desdeDto(hijo, carpeta));
                }
            }
            return carpeta;
        }
        DeltaMatroide dm = nodo.deltaMatroide == null ? null : desdeDto(nodo.deltaMatroide);
        ElementoFolder hoja = new ElementoFolder(false, nodo.nombre, dm, padre);
        hoja.expandContra = nodo.expandido;
        return hoja;
    }

    private static DeltaMatroide desdeDto(DeltaMatroideDto dto) {
        LinkedList<LinkedList<Integer>> familia = new LinkedList<>();
        for (List<Integer> factible : dto.familia) {
            familia.add(new LinkedList<>(factible));
        }
        // Este constructor recalcula la huella y la tabla de frecuencias.
        DeltaMatroide dm = new DeltaMatroide(familia, dto.nombre, dto.etiquetas);
        dm.setCampoDeOperacion(dto.campo);
        dm.setM(dto.matriz);
        dm.setOrdenar(dto.ordenar);
        switch (dto.modoFactibles) {
            case 0:
                dm.setModeFactibles0();
                break;
            case 2:
                dm.setModeFactibles2();
                break;
            default:
                dm.setModeFactibles1();
                break;
        }
        return dm;
    }

    // -------------------------------------------------------------- DTOs

    private static final class ArchivoDto {
        int version;
        NodoDto raiz;
    }

    private static final class NodoDto {
        String nombre;
        boolean carpeta;
        boolean expandido;
        List<NodoDto> hijos;
        DeltaMatroideDto deltaMatroide;
    }

    private static final class DeltaMatroideDto {
        String nombre;
        String[] etiquetas;
        /** 0 = GF(2), 1 = GF(3); -1 si se construyó desde una familia. */
        int campo;
        int modoFactibles;
        boolean ordenar;
        /** Matriz de adyacencia original; null si se construyó desde una familia. */
        byte[][] matriz;
        List<List<Integer>> familia;
    }
}
