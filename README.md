# ProyectoDelta

[![Pruebas](https://github.com/tamayodiego/ProyectoDelta/actions/workflows/pruebas.yml/badge.svg)](https://github.com/tamayodiego/ProyectoDelta/actions/workflows/pruebas.yml)

Paquete computacional de escritorio (Java + Swing) para la investigación en
**delta-matroides**: genera delta-matroides a partir de matrices simétricas o
antisimétricas sobre GF(2) y GF(3), y permite estudiarlas con operaciones como
twist, menores, isomorfismo y clases de delta-equivalencia.

Proyecto universitario de 2018, rescatado y modernizado en 2026.

## Qué hace

Desde el menú principal:

| Herramienta | Descripción |
|---|---|
| DeltaMatroides desde Matrices Principales | Analiza todas las submatrices principales de una matriz y construye la delta-matroide de las no singulares sobre GF(2) o GF(3). |
| Validar DeltaMatroide | Revisa si una familia de conjuntos es una delta-matroide. |
| Twisting | Aplica twist (diferencia simétrica) con un conjunto factible. |
| Cálculo de Menores | Menores por borrado y contracción. |
| Evaluar Isomorfismo | Busca una biyección entre dos delta-matroides. |
| Clases de Delta-Equivalencia | Agrupa delta-matroides equivalentes. |
| Generar Orientaciones | Genera orientaciones a partir de una matriz. |
| Propiedades y atributos | Huella, tabla de frecuencias y detalle de los factibles. |
| Lista de Delta-Matroides | Organiza el área de trabajo en carpetas. |

Matrices de entrada aceptadas: simétricas con entradas 0/1, o antisimétricas
con entradas -1/0/1 y diagonal en 0.

## Requisitos

- JDK 17 o superior (probado con Eclipse Temurin 17).
- No hace falta instalar Maven: el proyecto trae el *Maven Wrapper*
  (`mvnw` / `mvnw.cmd`), que descarga la versión correcta la primera vez.

## Ejecutar

```bash
./mvnw package                        # genera target/ProyectoDelta.jar
java -jar target/ProyectoDelta.jar    # interfaz gráfica
java -jar target/ProyectoDelta.jar -G # modo consola
```

En Windows se usa `mvnw.cmd package`. El `.jar` incluye todo lo necesario
(también Gson), así que se puede copiar y abrir en cualquier equipo con
Java 17.

Para desarrollar, abre la carpeta en **VS Code** con el *Extension Pack for
Java* (detecta el `pom.xml`) y usa F5 para ejecutar y depurar, o ábrela
directamente en **NetBeans** o **IntelliJ IDEA** como proyecto Maven. Los
archivos `.form` siguen funcionando con el editor visual de NetBeans.

El área de trabajo se guarda al cerrar en `areaDeTrabajo.json`, en la carpeta
desde donde se ejecuta la app. *Exportar* e *Importar* usan archivos `.dmj`
con el mismo formato.

## Pruebas

```bash
./mvnw test
```

Corre las pruebas con JUnit (`src/test/java/`):

- `Objetos/DeterminanteTest`: determinante exacto contra expansión por
  cofactores y contra eliminación módulo primos.
- `caracterizacion/`: "golden master" de 164 delta-matroides (familia,
  huella, frecuencias y determinantes) guardado en
  `src/test/resources/golden/`. Si un cambio altera algún resultado, la
  prueba lo señala. Para regenerarlo a propósito:
  `./mvnw test -Dactualizar.golden=true`.
- `persistencia/`: guardar y cargar el área de trabajo en JSON.
- `Recursos/ValidarTest`: validación de matrices de entrada.

## Estructura

```
ProyectoDelta/
├── pom.xml                  # proyecto Maven (Java 17, Gson, JUnit)
├── mvnw, mvnw.cmd, .mvn/    # Maven Wrapper
├── src/main/java/
│   ├── Tested/              # Main (arranque, modo consola, guardado)
│   ├── Formas/              # ventanas Swing (con sus .form de NetBeans)
│   ├── Objetos/             # modelo: DeltaMatroide, Determinante, Huella...
│   ├── Recursos/            # validación, árbol de carpetas, utilidades
│   ├── persistencia/        # área de trabajo en JSON
│   └── Excepciones/
├── src/main/resources/      # imágenes
├── src/test/                # pruebas JUnit y archivos golden
├── generador.c              # utilidad aparte: genera matrices aleatorias
└── LICENSE
```

## Cambios de 2026 respecto al original

- **Determinante exacto.** El original usaba eliminación gaussiana con
  `float` y comparaba pivotes con `== 0`; un residuo de redondeo se tomaba
  como pivote y el resultado se corrompía. Desde 12×12 había submatrices mal
  clasificadas (en 15×15, decenas o cientos por matriz). Ahora se usa el
  algoritmo de Bareiss con enteros, que es exacto.
- **Persistencia en JSON** en lugar de serialización nativa de Java
  (`data.00` / `.dma` → `areaDeTrabajo.json` / `.dmj`), con escritura segura
  y respaldo automático si el archivo no se puede leer. Los archivos del
  formato anterior ya no se pueden abrir.
- **Más rápido.** Generar una delta-matroide de 15×15 pasó de ~0.9 s a
  ~0.2 s, y cargar el área de trabajo, de segundos a milisegundos.
- **Validación en modo consola:** ahora acepta antisimétricas cuyos -1 están
  solo debajo de la diagonal.
- El modelo (`Objetos/`, `Recursos/`) ya no depende de la clase `Main`.
- Pruebas automáticas y proyecto Maven (antes NetBeans + Ant), con un
  `.jar` ejecutable que incluye sus dependencias.

## Créditos

Proyecto original (2018) desarrollado en equipo:

- Diego Leonardo Frausto Tamayo
- Saucedo
- Thor
- vonn_0132

Los nombres de coautores son los que aparecen en las etiquetas `@author` del
código original.

## Licencia

[GPL-3.0](LICENSE), la licencia que ya declaraba el proyecto original.
