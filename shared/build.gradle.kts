import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidLibrary {
        namespace = "rsanzjo.entreteclasytinta.template.shared"
        compileSdk = 36
        minSdk = 24

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }

        // Necesario para que los recursos de Compose (composeResources) viajen dentro del APK.
        androidResources {
            enable = true
        }

        // Tests unitarios en la JVM: los de iOS necesitan un simulador y no corren en Windows.
        withHostTest {}
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.mock)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.compose.ui.backhandler)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.material.icons.core)

            implementation(libs.jetbrains.lifecycle.viewmodel.compose)
            implementation(libs.jetbrains.lifecycle.runtime.compose)
            implementation(libs.jetbrains.navigation.compose)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.koin.core)
            implementation(libs.koin.core.viewmodel)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            implementation(project.dependencies.platform(libs.supabase.bom))
            implementation(libs.supabase.auth)
            implementation(libs.supabase.postgrest)
        }

        androidMain.dependencies {
            // Login nativo de Google (Credential Manager). En iOS exigiría enlazar el SDK GoogleSignIn.
            implementation(libs.supabase.compose.auth)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.security.crypto)
            implementation(libs.koin.android)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

// Configuración por entorno: variable de entorno > local.properties > valor de relleno (para que
// CI y un clon recién hecho compilen sin secretos). Se genera como `AppConfig` en commonMain.
val localProperties = Properties().apply {
    rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}

fun configValue(name: String, default: String): String =
    providers.environmentVariable(name).orNull ?: localProperties.getProperty(name) ?: default

val generateAppConfig by tasks.registering {
    val values = mapOf(
        "SUPABASE_URL" to configValue("SUPABASE_URL", "https://example.supabase.co"),
        "SUPABASE_PUBLISHABLE_KEY" to configValue("SUPABASE_PUBLISHABLE_KEY", "publishable-key"),
        "GOOGLE_WEB_CLIENT_ID" to configValue("GOOGLE_WEB_CLIENT_ID", "")
    )
    val outputDir = layout.buildDirectory.dir("generated/appConfig/commonMain/kotlin")
    inputs.properties(values)
    outputs.dir(outputDir)
    // Con puntos (y no como ruta) para que scripts/new-project lo renombre junto al resto del paquete.
    val configPackage = "rsanzjo.entreteclasytinta.template.config"
    inputs.property("configPackage", configPackage)
    doLast {
        val file = outputDir.get().file("${configPackage.replace('.', '/')}/AppConfig.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            buildString {
                appendLine("package $configPackage")
                appendLine()
                appendLine("/** Generado por `generateAppConfig` (shared/build.gradle.kts). No editar. */")
                appendLine("object AppConfig {")
                values.forEach { (name, value) ->
                    val escaped = value.replace("\\", "\\\\").replace("\"", "\\\"").replace("$", "\\$")
                    appendLine("    const val $name = \"$escaped\"")
                }
                appendLine("}")
            }
        )
    }
}

kotlin.sourceSets.commonMain {
    kotlin.srcDir(generateAppConfig)
}

compose.resources {
    packageOfResClass = "rsanzjo.entreteclasytinta.template.resources"
    generateResClass = always
}
