# Convenciones del proyecto

Proyecto KMP (Android + iOS) con UI en Compose Multiplatform. Arquitectura base reutilizable para proyectos nuevos.

## Capas y dependencias

`presentation` → `domain` ← `data`. `domain` no depende de nada del resto; `presentation` no conoce
Ktor ni los DTOs de `data`.

- Los errores de red se traducen a `DomainError` en `data` (`Throwable.toDomainError()`); la UI solo
  trabaja con `DomainError` (`Throwable.toErrorMessageRes()`).
- Casos de uso e interfaces de repositorio en `domain`; implementaciones en `data/repository/impl` y
  `domain/useCase/impl`.

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

Comentarios y KDoc en español. Strings de UI en `composeResources/values/strings.xml`.

## Añadir una feature

1. `presentation/feature/<área>/<nombre>/<Nombre>Contract.kt`: `UiState`, `Action`, `Effect`.
2. `<Nombre>ViewModel.kt` extendiendo `ViewModelBase`, con su test en `commonTest` (fakes de los casos de uso).
3. `<Nombre>Screen.kt` con su `@Preview`.
4. Destino `@Serializable` en `navigation/<grafo>/` y `composable<...>` en el grafo correspondiente.
5. Caso de uso, repositorio y llamada a `AppApi` si la feature necesita datos; registrarlos en `di/AppModules.kt`.
6. `viewModelOf(::<Nombre>ViewModel)` en `presentationModule` y los textos en `strings.xml`.

## Comandos

- Compilar Android: `./gradlew :androidApp:assembleDebug`
- Validar iOS en Windows: `./gradlew :shared:compileIosMainKotlinMetadata`
- Tests: `./gradlew :shared:testAndroidHostTest`
