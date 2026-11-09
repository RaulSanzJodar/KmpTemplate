# Prueba de humo de new-project.ps1. Uso: pwsh scripts/test-new-project.ps1
$ErrorActionPreference = 'Stop'
$script = Join-Path $PSScriptRoot 'new-project.ps1'
$work = Join-Path ([IO.Path]::GetTempPath()) ("kmp-template-test-" + [guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $work | Out-Null
$failures = @()

function Check([string]$name, [bool]$ok) {
    if ($ok) { Write-Host "PASS $name" } else { Write-Host "FAIL $name"; $script:failures += $name }
}

function Run([string]$name, [string]$package, [string]$dest) {
    & pwsh -NoProfile -File $script -Name $name -Package $package -Destination $dest *> $null
    return $LASTEXITCODE
}

try {
    # Destino ya existente: sale con 1 y no toca lo que hay.
    $existing = Join-Path $work 'existing'
    New-Item -ItemType Directory -Path $existing | Out-Null
    Set-Content (Join-Path $existing 'keep.txt') 'x'
    Check 'existing destination exits 1' ((Run 'MiApp' 'com.ejemplo.miapp' $existing) -eq 1)
    Check 'existing destination left intact' ((Get-ChildItem $existing).Count -eq 1)

    # Paquetes inválidos: sale con 1 y no crea el destino.
    foreach ($bad in @('Com.Ejemplo.App', 'ejemplo', 'com.ejemplo.in', 'com..app')) {
        $dest = Join-Path $work ("bad-" + [guid]::NewGuid().ToString('N'))
        Check "invalid package '$bad' exits 1" ((Run 'MiApp' $bad $dest) -eq 1)
        Check "invalid package '$bad' creates nothing" (-not (Test-Path $dest))
    }

    # Nombre inválido.
    $dest = Join-Path $work 'badname'
    Check 'invalid name exits 1' ((Run 'miApp' 'com.ejemplo.miapp' $dest) -eq 1)
    Check 'invalid name creates nothing' (-not (Test-Path $dest))

    # Caso válido.
    $ok = Join-Path $work 'MiApp'
    Check 'valid project exits 0' ((Run 'MiApp' 'com.ejemplo.miapp' $ok) -eq 0)
    Check 'package directory exists' (Test-Path (Join-Path $ok 'shared/src/commonMain/kotlin/com/ejemplo/miapp'))
    Check 'old rsanzjo directory is gone' (@(Get-ChildItem $ok -Recurse -Directory -Filter 'rsanzjo' -Force).Count -eq 0)
    Check 'rootProject.name replaced' ((Get-Content (Join-Path $ok 'settings.gradle.kts') -Raw) -match 'rootProject\.name = "MiApp"')
    $leftovers = Get-ChildItem $ok -Recurse -File -Force |
        Where-Object { $_.FullName -notmatch '[\\/]\.git[\\/]' -and $_.Extension -notin '.jar', '.png', '.webp' } |
        Select-String -Pattern 'rsanzjo\.entreteclasytinta\.template', 'KmpTemplate', 'TemplateApp' -List
    Check 'no template identifiers left' (@($leftovers).Count -eq 0)
    Check 'git repository initialised' (Test-Path (Join-Path $ok '.git'))
    Check 'scripts excluded' (-not (Test-Path (Join-Path $ok 'scripts')))
    Check 'docs excluded' (-not (Test-Path (Join-Path $ok 'docs')))
    Check 'application class renamed' (Test-Path (Join-Path $ok 'androidApp/src/main/kotlin/com/ejemplo/miapp/MiAppApp.kt'))
}
finally {
    Remove-Item $work -Recurse -Force -ErrorAction SilentlyContinue
}

if ($failures.Count -gt 0) { Write-Host "`n$($failures.Count) check(s) failed"; exit 1 }
Write-Host "`nAll checks passed"
