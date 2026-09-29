#!/usr/bin/env bash
# Compila src/ y test/ y ejecuta todas las pruebas con JUnit.
#
#   scripts/pruebas.sh                   ejecuta las pruebas
#   scripts/pruebas.sh --actualizar      regenera los archivos golden de
#                                        las pruebas de caracterización
set -euo pipefail

cd "$(dirname "$0")/.."

JUNIT="lib/junit-platform-console-standalone-6.1.3.jar"
SALIDA="build/pruebas"

if [[ ! -f "$JUNIT" ]]; then
    scripts/descargar-dependencias.sh
fi

PROPIEDADES=()
if [[ "${1:-}" == "--actualizar" ]]; then
    PROPIEDADES+=("-Dactualizar.golden=true")
fi

rm -rf "$SALIDA"
mkdir -p "$SALIDA"
find src test -name '*.java' > "$SALIDA/fuentes.txt"
javac -encoding UTF-8 -nowarn -cp "$JUNIT" -d "$SALIDA" @"$SALIDA/fuentes.txt"

# Copia los recursos (imágenes) junto a las clases, como hace NetBeans.
(cd src && find . -type f ! -name '*.java' ! -name '*.form') | while read -r f; do
    mkdir -p "$SALIDA/$(dirname "$f")"
    cp "src/$f" "$SALIDA/$f"
done

java ${PROPIEDADES[@]+"${PROPIEDADES[@]}"} -jar "$JUNIT" execute \
    --class-path "$SALIDA" \
    --scan-class-path \
    --disable-banner \
    --details=tree
