package Objetos;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Determinante exacto de una matriz cuadrada de enteros.
 *
 * La versión original de esta clase la escribió un compañero del equipo
 * con eliminación gaussiana en float. Se reemplazó en 2026 porque, al
 * comparar un pivote con == 0, un residuo de redondeo (p. ej. 2e-16) se
 * tomaba como pivote válido y el resultado se corrompía; desde 12x12
 * aparecían submatrices mal clasificadas como factibles o no factibles.
 *
 * Ahora se usa el algoritmo de Bareiss (1968): eliminación sin fracciones
 * donde cada división es exacta, así que trabaja solo con enteros. Cada
 * valor intermedio es un menor de la matriz original. Se calcula con long
 * y, si algún producto se desborda, se repite con BigInteger.
 */
public class Determinante implements Serializable {

    private final byte[][] matriz;

    public Determinante(byte[][] matriz) {
        this.matriz = matriz;
    }

    /**
     * Determinante exacto, con signo.
     */
    public BigInteger calcDetExacto() {
        try {
            return BigInteger.valueOf(bareissLong(matriz));
        } catch (ArithmeticException desbordamiento) {
            return bareissBigInteger(matriz);
        }
    }

    /**
     * Se conserva por compatibilidad con la interfaz anterior. Es exacto
     * mientras el determinante quepa en un double (|det| < 2^53).
     */
    public double calcDet() {
        return calcDetExacto().doubleValue();
    }

    static long bareissLong(byte[][] a) {
        int n = a.length;
        if (n == 0) {
            return 1;
        }
        long[][] m = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = a[i][j];
            }
        }

        int signo = 1;
        long pivoteAnterior = 1;
        for (int k = 0; k < n - 1; k++) {
            if (m[k][k] == 0) {
                int r = renglonConPivote(m, k);
                if (r < 0) {
                    return 0;
                }
                long[] t = m[k];
                m[k] = m[r];
                m[r] = t;
                signo = -signo;
            }
            for (int i = k + 1; i < n; i++) {
                for (int j = k + 1; j < n; j++) {
                    m[i][j] = Math.subtractExact(
                            Math.multiplyExact(m[k][k], m[i][j]),
                            Math.multiplyExact(m[i][k], m[k][j])) / pivoteAnterior;
                }
            }
            pivoteAnterior = m[k][k];
        }
        return signo * m[n - 1][n - 1];
    }

    static BigInteger bareissBigInteger(byte[][] a) {
        int n = a.length;
        if (n == 0) {
            return BigInteger.ONE;
        }
        BigInteger[][] m = new BigInteger[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = BigInteger.valueOf(a[i][j]);
            }
        }

        int signo = 1;
        BigInteger pivoteAnterior = BigInteger.ONE;
        for (int k = 0; k < n - 1; k++) {
            if (m[k][k].signum() == 0) {
                int r = -1;
                for (int i = k + 1; i < n && r < 0; i++) {
                    if (m[i][k].signum() != 0) {
                        r = i;
                    }
                }
                if (r < 0) {
                    return BigInteger.ZERO;
                }
                BigInteger[] t = m[k];
                m[k] = m[r];
                m[r] = t;
                signo = -signo;
            }
            for (int i = k + 1; i < n; i++) {
                for (int j = k + 1; j < n; j++) {
                    m[i][j] = m[k][k].multiply(m[i][j])
                            .subtract(m[i][k].multiply(m[k][j]))
                            .divide(pivoteAnterior);
                }
            }
            pivoteAnterior = m[k][k];
        }
        return signo < 0 ? m[n - 1][n - 1].negate() : m[n - 1][n - 1];
    }

    private static int renglonConPivote(long[][] m, int k) {
        for (int i = k + 1; i < m.length; i++) {
            if (m[i][k] != 0) {
                return i;
            }
        }
        return -1;
    }
}
