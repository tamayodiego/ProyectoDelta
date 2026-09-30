package caracterizacion;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import Objetos.DeltaMatroide;
import Objetos.SubMatriz;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de caracterización ("golden master") de DeltaMatroide.
 *
 * Registran lo que la app calcula HOY para un conjunto fijo de matrices
 * válidas, y fallan si un cambio posterior altera ese resultado.
 *
 * Grupo A (n <= 11): el determinante con float no falla en ese rango, así
 * que estos resultados son correctos y NO deben cambiar en ninguna fase.
 *
 * Grupo B (n = 12..15): el determinante con float puede clasificar mal
 * algunas submatrices. Se espera que cambie al corregir el determinante;
 * entonces se revisan las diferencias y se regenera el archivo golden.
 *
 * Para regenerar los archivos: ./mvnw test -Dactualizar.golden=true
 */
class CaracterizacionDeltaMatroideTest {

    private static final Path DIRECTORIO_GOLDEN = Paths.get("src", "test", "resources", "golden");
    private static final boolean ACTUALIZAR = Boolean.getBoolean("actualizar.golden");

    private static final int GF2 = 0;
    private static final int GF3 = 1;

    @Test
    void grupoA_hasta11_noDebeCambiar() throws IOException {
        verificar("grupoA_n1-11.txt", casos(1, 11, 3));
    }

    @Test
    void grupoB_12a15_cambioEsperadoAlCorregirDeterminante() throws IOException {
        verificar("grupoB_n12-15.txt", casos(12, 15, 2));
    }

    // ------------------------------------------------------------------

    private static final class Caso {
        final int n;
        final boolean simetrica;
        final int campo;
        final long semilla;

        Caso(int n, boolean simetrica, int campo, long semilla) {
            this.n = n;
            this.simetrica = simetrica;
            this.campo = campo;
            this.semilla = semilla;
        }

        String clave() {
            return String.format("n=%d tipo=%s campo=%s semilla=%d",
                    n, simetrica ? "SIM" : "ANTI", campo == GF2 ? "GF2" : "GF3", semilla);
        }
    }

    private static List<Caso> casos(int nMin, int nMax, int semillas) {
        List<Caso> casos = new ArrayList<>();
        for (int n = nMin; n <= nMax; n++) {
            for (boolean simetrica : new boolean[] {true, false}) {
                for (int campo : new int[] {GF2, GF3}) {
                    for (long s = 1; s <= semillas; s++) {
                        casos.add(new Caso(n, simetrica, campo, s));
                    }
                }
            }
        }
        return casos;
    }

    /**
     * Matriz aleatoria válida: simétrica con entradas 0/1, o antisimétrica
     * con entradas -1/0/1 y
     * diagonal en 0. La semilla incluye n y el tipo para que cada caso sea
     * independiente de los demás.
     */
    static byte[][] matriz(int n, boolean simetrica, long semilla) {
        Random rnd = new Random(semilla * 1_000 + n * 10 + (simetrica ? 1 : 2));
        byte[][] m = new byte[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (simetrica) {
                    byte v = (byte) rnd.nextInt(2);
                    m[i][j] = v;
                    m[j][i] = v;
                } else if (i != j) {
                    byte v = (byte) (rnd.nextInt(3) - 1);
                    m[i][j] = v;
                    m[j][i] = (byte) -v;
                }
            }
        }
        return m;
    }

    /**
     * Revisa la estructura directamente en lugar de usar Recursos.Validar,
     * porque Validar decide el tipo buscando negativos solo arriba de la
     * diagonal y rechaza antisimétricas como [[0,1],[-1,0]].
     */
    private static boolean esValida(byte[][] m, boolean simetrica) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                if (simetrica) {
                    if ((m[i][j] != 0 && m[i][j] != 1) || m[i][j] != m[j][i]) {
                        return false;
                    }
                } else if (m[i][j] < -1 || m[i][j] > 1 || m[i][j] != -m[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static String describir(Caso c) {
        byte[][] m = matriz(c.n, c.simetrica, c.semilla);
        assertTrue(esValida(m, c.simetrica), "la matriz generada no es válida: " + c.clave());

        DeltaMatroide dm = new DeltaMatroide(m, c.campo);

        List<Integer> determinantes = new ArrayList<>();
        for (SubMatriz s : dm.getDeltas()) {
            determinantes.add(s.getDeterminante());
        }

        return c.clave()
                + " | matriz=" + filas(m)
                + " | toString=" + dm.toString().trim()
                + " | familia=" + dm.getFamilia().size()
                + " sha=" + sha256(dm.getFamilia().toString())
                + " | huella=" + dm.getHuella()
                + " | frecuencias=" + Arrays.toString(dm.getTablaFrecuencias())
                + " | determinantes sha=" + sha256(determinantes.toString());
    }

    private static void verificar(String archivo, List<Caso> casos) throws IOException {
        Map<String, String> actual = new LinkedHashMap<>();
        for (Caso c : casos) {
            actual.put(c.clave(), describir(c));
        }

        Path ruta = DIRECTORIO_GOLDEN.resolve(archivo);
        if (ACTUALIZAR) {
            Files.createDirectories(DIRECTORIO_GOLDEN);
            Files.write(ruta, actual.values(), StandardCharsets.UTF_8);
            return;
        }
        if (!Files.exists(ruta)) {
            fail("No existe " + ruta + ". Genéralo con: ./mvnw test -Dactualizar.golden=true");
        }

        Map<String, String> esperado = new LinkedHashMap<>();
        for (String linea : Files.readAllLines(ruta, StandardCharsets.UTF_8)) {
            if (!linea.isEmpty()) {
                esperado.put(linea.substring(0, linea.indexOf(" | ")), linea);
            }
        }

        List<String> diferencias = new ArrayList<>();
        for (Map.Entry<String, String> e : actual.entrySet()) {
            String previo = esperado.get(e.getKey());
            if (!e.getValue().equals(previo)) {
                diferencias.add("  " + e.getKey()
                        + "\n    esperado: " + previo
                        + "\n    obtenido: " + e.getValue());
            }
        }
        if (esperado.size() != actual.size()) {
            diferencias.add("  el archivo golden tiene " + esperado.size()
                    + " casos y la prueba generó " + actual.size());
        }
        if (!diferencias.isEmpty()) {
            fail(diferencias.size() + " de " + actual.size() + " casos cambiaron respecto a "
                    + ruta + ":\n" + String.join("\n", diferencias));
        }
    }

    private static String filas(byte[][] m) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m.length; i++) {
            if (i > 0) {
                sb.append(';');
            }
            for (int j = 0; j < m.length; j++) {
                if (j > 0) {
                    sb.append(',');
                }
                sb.append(m[i][j]);
            }
        }
        return sb.toString();
    }

    private static String sha256(String texto) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                sb.append(String.format("%02x", hash[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
