package persistencia;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Objetos.DeltaMatroide;
import Recursos.DatosSerializados;
import Recursos.ElementoFolder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AreaDeTrabajoJsonTest {

    @TempDir
    Path carpeta;

    // ------------------------------------------------------ ida y vuelta

    @Test
    void idaYVuelta_conservaArbolDeltaMatroidesYBanderas() throws Exception {
        DatosSerializados original = areaDeEjemplo();
        Path ruta = carpeta.resolve("area.json");

        AreaDeTrabajoJson.guardar(original, ruta);
        DatosSerializados cargado = AreaDeTrabajoJson.cargar(ruta);

        assertArrayEquals(original.getFlags(), cargado.getFlags());
        assertMismoNodo(original.getMatroides(), cargado.getMatroides(), null);
    }

    @Test
    void guardarDosVeces_produceElMismoArchivo() throws Exception {
        Path a = carpeta.resolve("a.json");
        Path b = carpeta.resolve("b.json");
        AreaDeTrabajoJson.guardar(areaDeEjemplo(), a);
        AreaDeTrabajoJson.guardar(AreaDeTrabajoJson.cargar(a), b);
        assertEquals(leer(a), leer(b));
    }

    @Test
    void elJsonEsLegibleYVersionado() throws Exception {
        Path ruta = carpeta.resolve("area.json");
        AreaDeTrabajoJson.guardar(areaDeEjemplo(), ruta);
        String json = leer(ruta);
        assertTrue(json.startsWith("{\"version\":1,"), json.substring(0, 40));
        assertTrue(json.contains("\"sinValidarMatriz\":true"));
        assertTrue(json.contains("\"nombre\":\"Twst's\""));
    }

    // --------------------------------------------------- escritura segura

    @Test
    void guardar_reemplazaElArchivoYNoDejaTemporales() throws Exception {
        Path ruta = carpeta.resolve("area.json");
        Files.write(ruta, "contenido anterior".getBytes(StandardCharsets.UTF_8));

        AreaDeTrabajoJson.guardar(areaDeEjemplo(), ruta);

        assertTrue(leer(ruta).startsWith("{\"version\":1"));
        try (Stream<Path> archivos = Files.list(carpeta)) {
            List<String> nombres = archivos.map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
            assertEquals(List.of("area.json"), nombres);
        }
    }

    // --------------------------------------------------- archivos malos

    @Test
    void cargar_jsonMalFormado_lanzaFormatoInvalido() throws IOException {
        Path ruta = escribir("roto.json", "{\"version\": 1, \"raiz\": {");
        assertThrows(AreaDeTrabajoJson.FormatoInvalidoException.class,
                () -> AreaDeTrabajoJson.cargar(ruta));
    }

    @Test
    void cargar_archivoVacio_lanzaFormatoInvalido() throws IOException {
        Path ruta = escribir("vacio.json", "");
        assertThrows(AreaDeTrabajoJson.FormatoInvalidoException.class,
                () -> AreaDeTrabajoJson.cargar(ruta));
    }

    @Test
    void cargar_versionFutura_lanzaFormatoInvalido() throws IOException {
        Path ruta = escribir("futuro.json",
                "{\"version\":99,\"raiz\":{\"nombre\":\"r\",\"carpeta\":true,\"hijos\":[]}}");
        AreaDeTrabajoJson.FormatoInvalidoException e = assertThrows(
                AreaDeTrabajoJson.FormatoInvalidoException.class,
                () -> AreaDeTrabajoJson.cargar(ruta));
        assertTrue(e.getMessage().contains("99"));
    }

    @Test
    void cargar_serializacionJavaAntigua_lanzaFormatoInvalido() throws IOException {
        // Cabecera de un archivo de ObjectOutputStream (como el data.00 viejo).
        Path ruta = carpeta.resolve("data.00");
        Files.write(ruta, new byte[] {(byte) 0xAC, (byte) 0xED, 0x00, 0x05, 0x70});
        assertThrows(AreaDeTrabajoJson.FormatoInvalidoException.class,
                () -> AreaDeTrabajoJson.cargar(ruta));
    }

    // ---------------------------------------------------------- ejemplo

    /**
     * Raíz con la carpeta "Twst's" por defecto, más: una delta-matroide de
     * una matriz simétrica (GF2, con twist, ordenar y modo 0), una
     * subcarpeta colapsada con otra subcarpeta que tiene una antisimétrica
     * (GF3), y un menor, que se construye desde una familia (sin matriz ni
     * campo).
     */
    private static DatosSerializados areaDeEjemplo() throws Exception {
        DatosSerializados datos = new DatosSerializados();
        datos.getFlags()[1] = true;
        datos.getFlags()[3] = true;
        ElementoFolder raiz = datos.getMatroides();

        DeltaMatroide simetrica = new DeltaMatroide(new byte[][] {
            {1, 1, 0, 0, 1, 0},
            {1, 0, 1, 0, 0, 1},
            {0, 1, 1, 1, 0, 0},
            {0, 0, 1, 0, 1, 1},
            {1, 0, 0, 1, 1, 0},
            {0, 1, 0, 1, 0, 0},
        }, 0, "A");
        simetrica.setOrdenar(true);
        simetrica.setModeFactibles0();
        simetrica.twist(simetrica.getFamilia().get(3));
        raiz.folder.add(new ElementoFolder(false, "A", simetrica, raiz));

        LinkedList<ElementoFolder> hijos = new LinkedList<>();
        ElementoFolder sub = new ElementoFolder(true, "Sub", hijos, raiz);
        sub.expandContra = false;
        raiz.folder.add(sub);

        LinkedList<ElementoFolder> nietos = new LinkedList<>();
        ElementoFolder subSub = new ElementoFolder(true, "SubSub", nietos, sub);
        hijos.add(subSub);

        DeltaMatroide antisimetrica = new DeltaMatroide(new byte[][] {
            {0, 1, -1, 0, 1},
            {-1, 0, 1, 1, 0},
            {1, -1, 0, 0, -1},
            {0, -1, 0, 0, 1},
            {-1, 0, 1, -1, 0},
        }, 1, "B");
        antisimetrica.setModeFactibles2();
        nietos.add(new ElementoFolder(false, "B", antisimetrica, subSub));

        DeltaMatroide menor = simetrica.getMenorBorrado("2");
        hijos.add(new ElementoFolder(false, menor.getNombreFam(), menor, sub));

        return datos;
    }

    // ------------------------------------------------------- aserciones

    private static void assertMismoNodo(ElementoFolder esperado, ElementoFolder real,
            ElementoFolder padreReal) {
        assertEquals(esperado.getNombre(), real.getNombre());
        assertEquals(esperado.isIsFolder(), real.isIsFolder());
        assertEquals(esperado.expandContra, real.expandContra, esperado.getRuta());
        assertSame(padreReal, real.getNodoPadre(), "padre de " + esperado.getRuta());
        assertEquals(esperado.getRuta(), real.getRuta());

        if (esperado.isIsFolder()) {
            assertEquals(esperado.getFolder().size(), real.getFolder().size(), esperado.getRuta());
            for (int i = 0; i < esperado.getFolder().size(); i++) {
                assertMismoNodo(esperado.getFolder().get(i), real.getFolder().get(i), real);
            }
        } else if (esperado.getMatrode() == null) {
            assertNull(real.getMatrode());
        } else {
            assertMismaDeltaMatroide(esperado.getMatrode(), real.getMatrode());
        }
    }

    private static void assertMismaDeltaMatroide(DeltaMatroide e, DeltaMatroide r) {
        String quien = e.getNombreFam();
        assertEquals(e.getNombreFam(), r.getNombreFam());
        assertArrayEquals(e.getEtiquetas(), r.getEtiquetas(), quien);
        assertEquals(e.getCampoDeOperacion(), r.getCampoDeOperacion(), quien);
        assertEquals(e.getModoFactibles(), r.getModoFactibles(), quien);
        assertEquals(e.isOrdenar(), r.isOrdenar(), quien);
        assertArrayEquals(e.getM(), r.getM(), quien);
        assertEquals(e.getFamilia(), r.getFamilia(), quien);
        assertEquals(e.getHuella().toString(), r.getHuella().toString(), quien);
        assertArrayEquals(e.getTablaFrecuencias(), r.getTablaFrecuencias(), quien);
        assertEquals(e.toString(), r.toString(), quien);
        assertEquals(e.getResultado(), r.getResultado(), quien);
    }

    // -------------------------------------------------------- utilidades

    private Path escribir(String nombre, String contenido) throws IOException {
        Path ruta = carpeta.resolve(nombre);
        Files.write(ruta, contenido.getBytes(StandardCharsets.UTF_8));
        return ruta;
    }

    private static String leer(Path ruta) throws IOException {
        return new String(Files.readAllBytes(ruta), StandardCharsets.UTF_8);
    }
}
