# Kmp Template

Plantilla Kotlin Multiplatform (Android + iOS) con la UI compartida en Compose Multiplatform y
Supabase como backend. Incluye la arquitectura completa (dominio, datos, presentación MVI), la
autenticación montada (correo y contraseña, registro, recuperación de contraseña, Google y Apple) y
una pantalla Home de ejemplo como patrón a copiar.

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
  Es también el `applicationId`, el bundle id de iOS y el esquema de los deep links.
- Por defecto el proyecto se crea junto a la plantilla (`..\<Name>`). El destino no puede existir.
- El script copia la plantilla sin `build`, `.git`, `scripts` ni `docs`, mueve los directorios del paquete,
  sustituye los identificadores y hace `git init`.
- Después, crea `local.properties` a partir de `local.properties.example` (ver [Configurar](#configurar)).

`pwsh scripts/test-new-project.ps1` comprueba el script (validaciones y un proyecto de prueba).

## Estructura

| Ruta | Contenido |
|---|---|
| `shared/` | Dominio, datos y UI (Compose Multiplatform). Targets: Android, `iosArm64`, `iosSimulatorArm64`, `iosX64`. |
| `androidApp/` | `MainActivity`, `Application` (arranca Koin), manifest y recursos. |
| `iosApp/` | Proyecto Xcode (SwiftUI) que aloja el framework `Shared`. |
| `supabase/migrations/` | SQL del esquema (tablas, RLS, triggers). |
| `config/detekt/` | Reglas de análisis estático. |
| `.github/workflows/` | CI. |

Dentro de `shared/src/commonMain/kotlin/<paquete>`:

- `domain/` – entidades, interfaces de repositorio, casos de uso (+ `impl/`) y `DomainError`.
- `data/` – cliente de Supabase (`createAppSupabaseClient`), DTOs, `ApiErrorMapper`, repositorios `impl/`.
- `presentation/common/` – `ViewModelBase`, `BaseUiState`, navegación, componentes comunes.
- `presentation/feature/<área>/<pantalla>/` – `Contract`, `ViewModel`, `Screen`.
- `common/platform/` – `SecureStorage` y `platformModule` (`expect`; `actual` en `androidMain` e `iosMain`).
- `ui/theme/`, `di/`, `App.kt` (navegación raíz guiada por el estado de la sesión).

## Stack

Compose Multiplatform, Navigation (rutas tipadas), Coil 3, Koin, supabase-kt (Auth + Postgrest) sobre
Ktor, kotlinx.serialization.
Sesión: `SecureStorage` → `EncryptedSharedPreferences` (Android) / Keychain (iOS).

`supabase-kt` está fijado en 3.6.0, la última versión compilada con Kotlin 2.3.x; para subirla hay que
subir antes Kotlin a 2.4.

## Configurar

### 1. Claves

Copia `local.properties.example` como `local.properties` y rellena:

| Clave | Valor |
|---|---|
| `SUPABASE_URL` | Supabase > Project Settings > API > Project URL. |
| `SUPABASE_PUBLISHABLE_KEY` | Clave publicable (o `anon`). Nunca la `service_role`. |
| `GOOGLE_WEB_CLIENT_ID` | Opcional. Client ID de tipo *Web* de Google Cloud; activa el selector nativo de Google en Android. |

También valen como variables de entorno (tienen prioridad; así se configuran en CI). Sin ellas el
proyecto compila con valores de relleno, pero no puede conectarse. Se generan como `AppConfig`.

### 2. Base de datos

Aplica las migraciones de `supabase/migrations/` con la CLI (`supabase link` y `supabase db push`) o
pegándolas en el SQL Editor. `0001_profiles.sql` crea la tabla `profiles` con RLS y el trigger que
da de alta el perfil al registrarse el usuario.

### 3. Autenticación

En Supabase > Authentication:

- **URL Configuration > Redirect URLs**: añade `<paquete>://auth` (en la plantilla,
  `rsanzjo.entreteclasytinta.template://auth`). A esa dirección vuelve la app tras confirmar el
  correo, recuperar la contraseña o iniciar sesión con Google/Apple.
- **Providers > Google**: Client ID y secreto del cliente *Web* de Google Cloud. Para el selector
  nativo en Android, crea además un cliente *Android* con el paquete y el SHA-1 de la firma.
- **Providers > Apple**: Services ID, Team ID y clave de Sign in with Apple.
- La longitud mínima de contraseña debe coincidir con `MIN_PASSWORD_LENGTH` (6).

En iOS, Google y Apple usan el flujo por navegador. Para el selector nativo hay que añadir el SDK
GoogleSignIn al proyecto Xcode, el módulo `compose-auth` a `iosMain` y la capability *Sign in with Apple*.

### 4. Firma de release (Android)

Copia `keystore.properties.example` como `keystore.properties` y rellénalo. Sin él,
`assembleRelease` genera un APK sin firmar. El release va minificado con R8.

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

## Calidad

```
./gradlew :shared:testAndroidHostTest   # tests
./gradlew detekt                        # análisis estático
```

Los tests de iOS necesitan un simulador y no corren en Windows. El workflow `CI` ejecuta lint, tests,
build de Android (debug y release) y la validación de iOS en cada push a `main` y en cada pull
request; el job de simulador iOS se lanza a mano.

## Notas

- En Windows/Linux no se puede compilar el target iOS. `./gradlew :shared:compileIosMainKotlinMetadata`
  valida igualmente el código de `commonMain` e `iosMain`.
- La sesión cifrada queda fuera de las copias de seguridad de Android: su clave no sale del dispositivo.
