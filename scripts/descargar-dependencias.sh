#!/usr/bin/env bash
# Descarga las librerías de lib/ desde Maven Central y verifica su SHA-1.
# lib/ no se versiona en git; ejecuta este script después de clonar.
set -euo pipefail

cd "$(dirname "$0")/.."
mkdir -p lib

descargar() {
    local grupo="$1" artefacto="$2" version="$3"
    local jar="$artefacto-$version.jar"
    local url="https://repo1.maven.org/maven2/${grupo//.//}/$artefacto/$version/$jar"

    if [[ -f "lib/$jar" ]]; then
        echo "ya existe: lib/$jar"
        return
    fi

    echo "descargando $jar"
    curl -sfL -o "lib/$jar" "$url"
    local esperado obtenido
    esperado="$(curl -sfL "$url.sha1" | cut -c1-40)"
    obtenido="$(shasum -a 1 "lib/$jar" | cut -d' ' -f1)"
    if [[ "$esperado" != "$obtenido" ]]; then
        rm -f "lib/$jar"
        echo "ERROR: SHA-1 no coincide para $jar" >&2
        exit 1
    fi
}

descargar org.junit.platform junit-platform-console-standalone 6.1.3
descargar com.google.code.gson gson 2.14.0
