package Objetos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.Random;
import org.junit.jupiter.api.Test;

/**
 * Pruebas del determinante exacto (Bareiss).
 *
 * Las referencias son algoritmos independientes de Bareiss: expansión por
 * cofactores (Laplace) para matrices chicas y eliminación gaussiana
 * directa módulo un primo para cualquier tamaño.
 */
class DeterminanteTest {

    private static long det(byte[][] m) {
        return new Determinante(m).calcDetExacto().longValueExact();
    }

    // ---------------------------------------------------------- casos fijos

    @Test
    void matrizVacia_esUno() {
        assertEquals(1, det(new byte[0][0]));
    }

    @Test
    void casosChicos() {
        assertEquals(5, det(new byte[][] {{5}}));
        assertEquals(-2, det(new byte[][] {{1, 2}, {3, 4}}));
        assertEquals(1, det(new byte[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}));
        assertEquals(0, det(new byte[][] {{0, 0}, {0, 0}}));
        assertEquals(0, det(new byte[][] {{1, 1}, {1, 1}}));
    }

    @Test
    void pivoteCero_obligaAIntercambiarRenglones() {
        assertEquals(-1, det(new byte[][] {{0, 1}, {1, 0}}));
        assertEquals(1, det(new byte[][] {{0, 1}, {-1, 0}}));
        assertEquals(2, det(new byte[][] {{0, 1, 1}, {1, 0, 1}, {1, 1, 0}}));
    }

    @Test
    void columnaDeCeros_esCero() {
        assertEquals(0, det(new byte[][] {{0, 1, 1}, {0, 0, 1}, {0, 1, 0}}));
    }

    /**
     * Matriz donde la versión anterior con float devolvía 8 (y con double,
     * 0): un pivote que debía ser 0 quedaba en ~2e-16 y se dividía entre él.
     */
    @Test
    void casoDondeFallabaLaVersionFloat() {
        byte[][] m = {
            {2, 1, 2, 1, 0, 2},
            {1, 2, 2, 0, 1, 2},
            {2, 2, 2, 0, 2, 1},
            {1, 0, 0, 0, 0, 1},
            {0, 1, 2, 0, 1, 0},
            {2, 2, 1, 1, 0, 1},
        };
        assertEquals(laplace(m), BigInteger.valueOf(det(m)));
        assertEquals(3, Math.abs(det(m)));
    }

    // ------------------------------------------------- matrices aleatorias

    @Test
    void validasAleatorias_hasta8_coincidenConLaplace() {
        Random rnd = new Random(1);
        for (int n = 1; n <= 8; n++) {
            for (int rep = 0; rep < 40; rep++) {
                byte[][] m = valida(n, rep % 2 == 0, rnd);
                assertEquals(laplace(m), new Determinante(m).calcDetExacto(), () -> texto(m));
            }
        }
    }

    @Test
    void validasAleatorias_hasta15_coincidenModuloPrimos() {
        Random rnd = new Random(2);
        long[] primos = {2, 3, 1_000_003, 998_244_353};
        for (int n = 1; n <= 15; n++) {
            for (int rep = 0; rep < 60; rep++) {
                byte[][] m = valida(n, rep % 2 == 0, rnd);
                BigInteger d = new Determinante(m).calcDetExacto();
                for (long p : primos) {
                    assertEquals(detModulo(m, p), d.mod(BigInteger.valueOf(p)).longValue(),
                            () -> "p=" + p + "\n" + texto(m));
                }
            }
        }
    }

    // ------------------------------------------------------ desbordamiento

    @Test
    void desbordamientoEnLong_usaBigInteger() {
        Random rnd = new Random(3);
        byte[][] m = new byte[20][20];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                m[i][j] = (byte) (rnd.nextInt(255) - 127);
            }
        }

        assertThrows(ArithmeticException.class, () -> Determinante.bareissLong(m));

        BigInteger d = new Determinante(m).calcDetExacto();
        assertEquals(Determinante.bareissBigInteger(m), d);
        for (long p : new long[] {1_000_003, 998_244_353, 2_147_483_647}) {
            assertEquals(detModulo(m, p), d.mod(BigInteger.valueOf(p)).longValue());
        }
    }

    // ---------------------------------------------------------- utilidades

    /** Simétrica 0/1, o antisimétrica -1/0/1 con diagonal en 0. */
    private static byte[][] valida(int n, boolean simetrica, Random rnd) {
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

    /** Expansión por cofactores sobre el primer renglón. */
    private static BigInteger laplace(byte[][] m) {
        int n = m.length;
        if (n == 0) {
            return BigInteger.ONE;
        }
        BigInteger total = BigInteger.ZERO;
        for (int c = 0; c < n; c++) {
            if (m[0][c] == 0) {
                continue;
            }
            byte[][] menor = new byte[n - 1][n - 1];
            for (int i = 1; i < n; i++) {
                for (int j = 0, k = 0; j < n; j++) {
                    if (j != c) {
                        menor[i - 1][k++] = m[i][j];
                    }
                }
            }
            BigInteger termino = BigInteger.valueOf(m[0][c]).multiply(laplace(menor));
            total = c % 2 == 0 ? total.add(termino) : total.subtract(termino);
        }
        return total;
    }

    /** Eliminación gaussiana en el campo de enteros módulo p (p primo). */
    private static long detModulo(byte[][] a, long p) {
        int n = a.length;
        long[][] m = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = Math.floorMod(a[i][j], p);
            }
        }
        long det = 1;
        for (int c = 0; c < n; c++) {
            int piv = -1;
            for (int r = c; r < n && piv < 0; r++) {
                if (m[r][c] != 0) {
                    piv = r;
                }
            }
            if (piv < 0) {
                return 0;
            }
            if (piv != c) {
                long[] t = m[c];
                m[c] = m[piv];
                m[piv] = t;
                det = (p - det) % p;
            }
            det = det * m[c][c] % p;
            long inv = BigInteger.valueOf(m[c][c]).modInverse(BigInteger.valueOf(p)).longValue();
            for (int r = c + 1; r < n; r++) {
                long f = m[r][c] * inv % p;
                if (f == 0) {
                    continue;
                }
                for (int k = c; k < n; k++) {
                    m[r][k] = Math.floorMod(m[r][k] - f * m[c][k] % p, p);
                }
            }
        }
        return det;
    }

    private static String texto(byte[][] m) {
        StringBuilder sb = new StringBuilder();
        for (byte[] fila : m) {
            sb.append(java.util.Arrays.toString(fila)).append('\n');
        }
        return sb.toString();
    }
}
