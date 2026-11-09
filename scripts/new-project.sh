#!/usr/bin/env bash
# Crea un proyecto nuevo a partir de esta plantilla KMP.
# Uso: scripts/new-project.sh <Name> <package> [destino]
#   scripts/new-project.sh MiApp com.ejemplo.miapp
set -euo pipefail

fail() { echo "new-project: $1" >&2; exit 1; }

[ $# -ge 2 ] || fail "uso: new-project.sh <Name> <package> [destino]"
NAME="$1"
PACKAGE="$2"
TEMPLATE_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
OLD_PACKAGE="rsanzjo.entreteclasytinta.template"
RESERVED=" abstract as assert boolean break byte case catch char class const continue default do double else enum extends false final finally float for fun goto if implements import in instanceof int interface is long native new null object package private protected public return short static strictfp super switch synchronized this throw throws transient true try typealias typeof val var void volatile when while "

# --- Validación: nada se crea hasta que todo es válido. ---
[[ "$NAME" =~ ^[A-Z][A-Za-z0-9]*$ ]] || fail "Name '$NAME' no es válido: debe ser PascalCase (p. ej. MiApp)."
IFS='.' read -r -a SEGMENTS <<< "$PACKAGE"
[ "${#SEGMENTS[@]}" -ge 2 ] || fail "Package '$PACKAGE' debe tener al menos dos segmentos (p. ej. com.ejemplo)."
for segment in "${SEGMENTS[@]}"; do
    [[ "$segment" =~ ^[a-z][a-z0-9]*$ ]] || fail "Package '$PACKAGE' no es válido: cada segmento va en minúsculas, empieza por letra y solo lleva letras o dígitos."
    [[ "$RESERVED" != *" $segment "* ]] || fail "Package '$PACKAGE' no es válido: '$segment' es una palabra reservada."
done
DEST="${3:-$(dirname "$TEMPLATE_ROOT")/$NAME}"
[ ! -e "$DEST" ] || fail "El destino '$DEST' ya existe."
DEST="$(mkdir -p "$DEST" && cd "$DEST" && pwd)"
case "$DEST/" in "$TEMPLATE_ROOT"/*) rmdir "$DEST"; fail "El destino no puede estar dentro de la plantilla.";; esac

# --- 1. Copia, sin artefactos de build ni material de la plantilla. ---
(cd "$TEMPLATE_ROOT" && tar --exclude=.git --exclude=build --exclude=.gradle --exclude=.idea \
    --exclude=.kotlin --exclude=.superpowers --exclude=.codegraph --exclude=./scripts --exclude=./docs \
    --exclude=local.properties -cf - .) | (cd "$DEST" && tar -xf -)

# --- 2. Mueve los directorios del paquete en cada raíz de código (la que contiene 'rsanzjo'). ---
PACKAGE_PATH="${PACKAGE//./\/}"
while IFS= read -r root; do
    parent="$(dirname "$root")"
    mv "$root" "$parent/__old_package"
    mkdir -p "$parent/$PACKAGE_PATH"
    mv "$parent/__old_package/entreteclasytinta/template/"* "$parent/$PACKAGE_PATH/"
    rm -rf "$parent/__old_package"
done < <(find "$DEST" -type d -name rsanzjo -path '*' -exec test -d '{}/entreteclasytinta/template' \; -print)

# --- 3. Sustituye identificadores en los ficheros de texto. ---
DISPLAY_NAME="$(echo "$NAME" | sed -E 's/([a-z0-9])([A-Z])/\1 \2/g')"
ESCAPED_OLD="${OLD_PACKAGE//./\\.}"
find "$DEST" -type f ! -name '*.jar' ! -name '*.png' ! -name '*.webp' ! -name '*.jpg' \
    ! -name '*.ttf' ! -name '*.otf' ! -name '*.ico' -print0 |
    xargs -0 sed -i \
        -e "s/$ESCAPED_OLD/$PACKAGE/g" \
        -e "s/TemplateApp/${NAME}App/g" \
        -e "s/Kmp Template/$DISPLAY_NAME/g" \
        -e "s/KmpTemplate/$NAME/g"

find "$DEST" -type f -name 'TemplateApp.kt' -exec sh -c 'mv "$1" "$(dirname "$1")/'"$NAME"'App.kt"' _ {} \;

# --- 4. Repositorio nuevo. ---
git -C "$DEST" init -q
echo "Proyecto '$NAME' creado en $DEST (paquete $PACKAGE)."
