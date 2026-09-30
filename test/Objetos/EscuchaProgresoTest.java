package Objetos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class EscuchaProgresoTest {

    @Test
    void generarDeltaMatroide_reportaEtapasYAvanceCompleto() {
        List<String> etapas = new ArrayList<>();
        List<int[]> avances = new ArrayList<>();
        EscuchaProgreso escucha = new EscuchaProgreso() {
            @Override
            public void etapa(String descripcion) {
                etapas.add(descripcion);
            }

            @Override
            public void avance(int hechas, int total) {
                avances.add(new int[] {hechas, total});
            }
        };

        byte[][] m = {
            {0, 1, 0, 1},
            {1, 1, 1, 0},
            {0, 1, 0, 1},
            {1, 0, 1, 1},
        };
        DeltaMatroide conEscucha = new DeltaMatroide(m, 0, "D", escucha);

        assertEquals(List.of("Generando combinaciones", "Analizando combinaciones", "Terminado"),
                etapas);
        // Subconjuntos de tamaño 2 a 4 de 4 elementos: 2^4 - 1 - 4 = 11.
        int total = 11;
        assertEquals(total, avances.size());
        for (int i = 0; i < total; i++) {
            assertEquals(i + 1, avances.get(i)[0]);
            assertEquals(total, avances.get(i)[1]);
        }

        // Escuchar el progreso no cambia el resultado.
        DeltaMatroide sinEscucha = new DeltaMatroide(m, 0, "D");
        assertEquals(sinEscucha.getFamilia(), conEscucha.getFamilia());
    }
}
