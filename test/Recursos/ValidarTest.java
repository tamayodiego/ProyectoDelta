package Recursos;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ValidarTest {

    private static ParValidacion validar(byte[][] m) {
        return new Validar().validarMatrizAdyacencia(m);
    }

    @Test
    void antisimetricaConNegativosSoloAbajoDeLaDiagonal_esValida() {
        ParValidacion r = validar(new byte[][] {{0, 1}, {-1, 0}});
        assertFalse(r.isTipoMatriz(), "debe detectarse como antisimétrica");
        assertTrue(r.isValidaEnSuTipo());
    }

    @Test
    void antisimetricaConNegativosArriba_esValida() {
        ParValidacion r = validar(new byte[][] {{0, -1, 1}, {1, 0, 0}, {-1, 0, 0}});
        assertFalse(r.isTipoMatriz());
        assertTrue(r.isValidaEnSuTipo());
    }

    @Test
    void simetrica01_esValida() {
        ParValidacion r = validar(new byte[][] {{1, 0, 1}, {0, 0, 1}, {1, 1, 1}});
        assertTrue(r.isTipoMatriz(), "debe detectarse como simétrica");
        assertTrue(r.isValidaEnSuTipo());
    }

    @Test
    void matrizDeCeros_esValida() {
        assertTrue(validar(new byte[3][3]).isValidaEnSuTipo());
    }

    @Test
    void invalidas() {
        assertFalse(validar(new byte[][] {{0, 1}, {0, 0}}).isValidaEnSuTipo(), "no simétrica");
        assertFalse(validar(new byte[][] {{2, 0}, {0, 0}}).isValidaEnSuTipo(), "entrada 2");
        assertFalse(validar(new byte[][] {{1, 1}, {-1, 0}}).isValidaEnSuTipo(),
                "antisimétrica con diagonal distinta de 0");
        assertFalse(validar(new byte[][] {{0, -1}, {-1, 0}}).isValidaEnSuTipo(),
                "negativos sin ser antisimétrica");
        assertFalse(validar(null).isValidaEnSuTipo());
    }
}
