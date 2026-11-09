<#
.SYNOPSIS
    Crea un proyecto nuevo a partir de esta plantilla KMP.
.EXAMPLE
    pwsh scripts/new-project.ps1 -Name MiApp -Package com.ejemplo.miapp
    pwsh scripts/new-project.ps1 -Name MiApp -Package com.ejemplo.miapp -Destination C:\proyectos\MiApp
#>
param(
    [Parameter(Mandatory)][string]$Name,
    [Parameter(Mandatory)][string]$Package,
    [string]$Destination
)

$ErrorActionPreference = 'Stop'

$templateRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$oldPackage = 'rsanzjo.entreteclasytinta.template'
$reserved = @(
    'abstract', 'as', 'assert', 'boolean', 'break', 'byte', 'case', 'catch', 'char', 'class', 'const',
    'continue', 'default', 'do', 'double', 'else', 'enum', 'extends', 'false', 'final', 'finally', 'float',
    'for', 'fun', 'goto', 'if', 'implements', 'import', 'in', 'instanceof', 'int', 'interface', 'is', 'long',
    'native', 'new', 'null', 'object', 'package', 'private', 'protected', 'public', 'return', 'short',
    'static', 'strictfp', 'super', 'switch', 'synchronized', 'this', 'throw', 'throws', 'transient', 'true',
    'try', 'typealias', 'typeof', 'val', 'var', 'void', 'volatile', 'when', 'while'
)

function Fail([string]$message) {
    [Console]::Error.WriteLine("new-project: $message")
    exit 1
}

# --- Validación: nada se crea hasta que todo es válido. ---
if ($Name -cnotmatch '^[A-Z][A-Za-z0-9]*$') {
    Fail "Name '$Name' no es válido: debe ser PascalCase (p. ej. MiApp)."
}
$segments = $Package.Split('.')
if ($segments.Count -lt 2) { Fail "Package '$Package' debe tener al menos dos segmentos (p. ej. com.ejemplo)." }
foreach ($segment in $segments) {
    if ($segment -cnotmatch '^[a-z][a-z0-9]*$') {
        Fail "Package '$Package' no es válido: cada segmento va en minúsculas, empieza por letra y solo lleva letras o dígitos."
    }
    if ($reserved -contains $segment) { Fail "Package '$Package' no es válido: '$segment' es una palabra reservada." }
}
if (-not $Destination) { $Destination = Join-Path (Split-Path $templateRoot -Parent) $Name }
$Destination = [IO.Path]::GetFullPath($Destination)
if (Test-Path $Destination) { Fail "El destino '$Destination' ya existe." }
if ($Destination.StartsWith($templateRoot, [StringComparison]::OrdinalIgnoreCase)) {
    Fail 'El destino no puede estar dentro de la plantilla.'
}

# --- 1. Copia, sin artefactos de build ni material de la plantilla. ---
New-Item -ItemType Directory -Path $Destination | Out-Null
& robocopy $templateRoot $Destination /E /NFL /NDL /NJH /NJS /NP `
    /XD .git build .gradle .idea .kotlin .superpowers .codegraph scripts docs `
    /XF local.properties | Out-Null
if ($LASTEXITCODE -ge 8) { Fail "robocopy falló con el código $LASTEXITCODE." }

# --- 2. Mueve los directorios del paquete en cada raíz de código (la que contiene 'rsanzjo'). ---
$packagePath = $segments -join [IO.Path]::DirectorySeparatorChar
$oldPackagePath = $oldPackage.Replace('.', [IO.Path]::DirectorySeparatorChar)
$roots = Get-ChildItem $Destination -Recurse -Directory -Filter 'rsanzjo' -Force |
    Where-Object { Test-Path (Join-Path $_.FullName 'entreteclasytinta/template') }
foreach ($root in $roots) {
    $parent = $root.Parent.FullName
    $staging = Join-Path $parent '__old_package'
    Move-Item $root.FullName $staging
    $target = Join-Path $parent $packagePath
    New-Item -ItemType Directory -Path $target -Force | Out-Null
    Get-ChildItem (Join-Path $staging 'entreteclasytinta/template') -Force |
        Move-Item -Destination $target
    Remove-Item $staging -Recurse -Force
}

# --- 3. Sustituye identificadores en los ficheros de texto. ---
$binary = '.jar', '.png', '.webp', '.jpg', '.ttf', '.otf', '.ico'
$displayName = $Name -creplace '(?<=[a-z0-9])(?=[A-Z])', ' '
$utf8 = New-Object System.Text.UTF8Encoding($false)
Get-ChildItem $Destination -Recurse -File -Force | Where-Object { $_.Extension -notin $binary } | ForEach-Object {
    $text = [IO.File]::ReadAllText($_.FullName)
    $new = $text.Replace($oldPackage, $Package).
        Replace('TemplateApp', "${Name}App").
        Replace('Kmp Template', $displayName).
        Replace('KmpTemplate', $Name)
    if ($new -ne $text) { [IO.File]::WriteAllText($_.FullName, $new, $utf8) }
}

# Las clases renombradas por contenido también cambian de nombre de fichero.
Get-ChildItem $Destination -Recurse -File -Filter 'TemplateApp.kt' -Force |
    ForEach-Object { Rename-Item $_.FullName "${Name}App.kt" }

# --- 4. Repositorio nuevo. ---
& git -C $Destination init -q
Write-Host "Proyecto '$Name' creado en $Destination (paquete $Package)."
