# Kmp Template

Plantilla Kotlin Multiplatform (Android + iOS) con la UI compartida en Compose Multiplatform. Incluye
la arquitectura completa (dominio, datos, presentación MVI), autenticación con renovación de token y
dos pantallas de ejemplo (Login y Home) como patrón a copiar.

## Crear un proyecto nuevo

```powershell
pwsh scripts/new-project.ps1 -Name MiApp -Package com.ejemplo.miapp
pwsh scripts/new-project.ps1 -Name MiApp -Package com.ejemplo.miapp -Destination C:\proyectos\MiApp
```

```bash
scripts/new-project.sh MiApp com.ejemplo.miapp [destino]
```

- `Name`: PascalCase (`MiApp`). Se usa como nombre del proyecto, de la clase `Application` (`MiAppApp`) y,
  separado por palabras, como nombre visible.
- `Package`: minúsculas, al menos dos segmentos, sin palabras reservadas de Kotlin/Java (`com.ejemplo.miapp`).
- Por defecto el proyecto se crea junto a la plantilla (`..\<Name>`). El destino no puede existir.
- El script copia la plantilla sin `build`, `.git`, `scripts` ni `docs`, mueve los directorios del paquete,
  sustituye los identificadores y hace `git init`.
- Después copia o crea `local.properties` con `sdk.dir` (el script no lo copia).

`pwsh scripts/test-new-project.ps1` comprueba el script (validaciones y un proyecto de prueba).

## Estructura

| Módulo | Contenido |
|---|---|
| `shared/` | Dominio, datos y UI (Compose Multiplatform). Targets: Android, `iosArm64`, `iosSimulatorArm64`, `iosX64`. |
| `androidApp/` | `MainActivity`, `Application` (arranca Koin), manifest y recursos. |
| `iosApp/` | Proyecto Xcode (SwiftUI) que aloja el framework `Shared`. |

Dentro de `shared/src/commonMain/kotlin/<paquete>`:

- `domain/` – entidades, interfaces de repositorio, casos de uso (+ `impl/`) y `DomainError`.
- `data/` – cliente Ktor (`createHttpClient`), `AppApi`, `SessionManager`, `ApiErrorMapper`, repositorios `impl/`.
- `presentation/common/` – `ViewModelBase`, `BaseUiState`, navegación, componentes comunes.
- `presentation/feature/<área>/<pantalla>/` – `Contract`, `ViewModel`, `Screen`.
- `common/platform/` – `SecureStorage` y `platformModule` (`expect`; `actual` en `androidMain` e `iosMain`).
- `ui/theme/`, `di/`, `App.kt`.

## Stack

Compose Multiplatform, Navigation (rutas tipadas), Coil 3, Koin, Ktor Client + kotlinx.serialization.
Sesión: `SecureStorage` → `EncryptedSharedPreferences` (Android) / Keychain (iOS).

## Configurar

- URL de la API: `API_BASE_URL` en `shared/.../data/datasource/api/client/HttpClientFactory.kt`
  (ahora `https://api.example.com/`). Los endpoints están en `AppApi` (`login`, `logout`, `profile`)
  y el refresco de token usa `POST refresh`.

## Ejecutar

### Android

```
./gradlew :androidApp:installDebug
```

o ejecutar la configuración `androidApp` en Android Studio.

### iOS (requiere macOS con Xcode)

1. Abrir `iosApp/iosApp.xcodeproj` en Xcode.
2. Ejecutar en un simulador o dispositivo. Una fase de build llama a
   `./gradlew :shared:embedAndSignAppleFrameworkForXcode` y enlaza el framework `Shared`.
3. Para firmar en dispositivo real, definir el *Team* en *Signing & Capabilities*.

## Tests

```
./gradlew :shared:testAndroidHostTest
```

Los tests de iOS necesitan un simulador y no corren en Windows.

## Notas

- En Windows/Linux no se puede compilar el target iOS. `./gradlew :shared:compileIosMainKotlinMetadata`
  valida igualmente el código de `commonMain` e `iosMain`.
