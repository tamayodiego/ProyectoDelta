package Objetos;

/**
 * Recibe el avance de un cálculo largo (por ejemplo, la generación de una
 * delta-matroide) sin que el modelo dependa de la interfaz gráfica.
 */
public interface EscuchaProgreso {

    /** El cálculo entró a una nueva etapa. */
    void etapa(String descripcion);

    /** Se completaron {@code hechas} de {@code total} unidades de trabajo. */
    void avance(int hechas, int total);

    /** Implementación que ignora todos los avisos. */
    EscuchaProgreso NINGUNO = new EscuchaProgreso() {
        @Override
        public void etapa(String descripcion) {
        }

        @Override
        public void avance(int hechas, int total) {
        }
    };
}
