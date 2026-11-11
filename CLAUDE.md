# Convenciones del proyecto

Proyecto KMP (Android + iOS) con UI en Compose Multiplatform y Supabase como backend. Arquitectura
base reutilizable para proyectos nuevos.

## Capas y dependencias

`presentation` → `domain` ← `data`. `domain` no depende de nada del resto; `presentation` no conoce
Supabase, Ktor ni los DTOs de `data`.

- Los errores del backend se traducen a `DomainError` en `data`: todo repositorio envuelve sus
  llamadas en `domainCall { }` (`Throwable.toDomainError()`). La UI solo trabaja con `DomainError`
  (`Throwable.toErrorMessageRes()`).
- Casos de uso e interfaces de repositorio en `domain`; implementaciones en `data/repository/impl` y
  `domain/useCase/impl`.
- Única excepción a la regla de capas: `presentation/common/auth/SocialSignIn` (`expect`/`actual`),
  porque el selector nativo de Google solo existe como composable.

## Supabase

- El cliente (`createAppSupabaseClient`) solo se inyecta en repositorios. DTOs `@Serializable` en
  `data/datasource/supabase/dto` con su `toDomain()`.
- Toda tabla nueva va en una migración de `supabase/migrations/` con RLS activado y políticas
  explícitas, siguiendo `0001_profiles.sql`. La app usa la clave publicable: lo que RLS no proteja
  es público.
- La sesión la gestiona supabase-kt (refresco automático) y se guarda cifrada en `SecureStorage`.
- La navegación entre la zona de autenticación y la privada la decide `App` a partir de
  `AuthUseCase.sessionState`. Los ViewModels no navegan al iniciar o cerrar sesión.
- La URL y las claves salen de `AppConfig` (generado desde `local.properties` o variables de
  entorno); nunca se escriben en el código.

## MVI

Cada pantalla tiene un `Contract` (`UiState`, `Action`, `Effect`), un `ViewModel` que extiende
`ViewModelBase<S, A, E>` y una `Screen`.

- El estado solo se modifica desde el ViewModel con `setState`; la vista envía intenciones con
  `manageAction`.
- Efectos de un solo uso (snackbars, etc.) con `sendEffect`; usa `Nothing` si no hay ninguno.
- Operaciones con carga y error con `launchWithLoading`; navegación con `navigateTo(NavHostEvent)`.

## Inyección de dependencias

Koin. Registrar repositorios y casos de uso en `dataModule`/`domainModule` y los ViewModels con
`viewModelOf(::XViewModel)` en `presentationModule` (`di/AppModules.kt`). Lo que solo puede construir cada
plataforma va en `platformModule` (`expect`/`actual`).

## Composables

Todo composable en un fichero propio lleva un `@Preview` (`@PreviewLightDark`) privado al final, envuelto
en `AppTheme`, con datos de ejemplo.

## Estilo

Comentarios y KDoc en español. Strings de UI en `composeResources/values/strings.xml`. Líneas de
120 columnas como máximo (`.editorconfig`); `./gradlew detekt` debe pasar.

## Añadir una feature

1. `presentation/feature/<área>/<nombre>/<Nombre>Contract.kt`: `UiState`, `Action`, `Effect`.
2. `<Nombre>ViewModel.kt` extendiendo `ViewModelBase`, con su test en `commonTest` (fakes de los casos de uso).
3. `<Nombre>Screen.kt` con su `@Preview`.
4. Destino `@Serializable` en `navigation/<grafo>/` y `composable<...>` en el grafo correspondiente.
5. Si necesita datos: migración SQL con RLS, DTO, repositorio sobre el cliente de Supabase (con su
   test usando `testSupabaseClient`) y caso de uso; registrarlos en `di/AppModules.kt`.
6. `viewModelOf(::<Nombre>ViewModel)` en `presentationModule` y los textos en `strings.xml`.

## Comandos

- Compilar Android: `./gradlew :androidApp:assembleDebug`
- Validar iOS en Windows: `./gradlew :shared:compileIosMainKotlinMetadata`
- Tests: `./gradlew :shared:testAndroidHostTest`
- Análisis estático: `./gradlew detekt`
