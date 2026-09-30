#!/usr/bin/env bash
# Compila src/ y ejecuta ProyectoDelta. Los argumentos se pasan a la app
# (por ejemplo: scripts/ejecutar.sh -G para el modo consola).
set -euo pipefail

cd "$(dirname "$0")/.."

GSON="lib/gson-2.14.0.jar"
SALIDA="build/app"

if [[ ! -f "$GSON" ]]; then
    scripts/descargar-dependencias.sh
fi

rm -rf "$SALIDA"
mkdir -p "$SALIDA"
find src -name '*.java' > "$SALIDA/fuentes.txt"
javac -encoding UTF-8 -nowarn -cp "$GSON" -d "$SALIDA" @"$SALIDA/fuentes.txt"

# Copia los recursos (imágenes) junto a las clases.
(cd src && find . -type f ! -name '*.java' ! -name '*.form') | while read -r f; do
    mkdir -p "$SALIDA/$(dirname "$f")"
    cp "src/$f" "$SALIDA/$f"
done

java -cp "$SALIDA:$GSON" Tested.Main "$@"
