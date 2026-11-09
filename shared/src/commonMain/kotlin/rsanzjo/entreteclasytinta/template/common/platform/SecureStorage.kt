package rsanzjo.entreteclasytinta.template.common.platform

import org.koin.core.module.Module

/** Almacén clave-valor cifrado por plataforma (EncryptedSharedPreferences en Android, Keychain en iOS). */
interface SecureStorage {
    fun getString(key: String): String?
    fun putString(key: String, value: String)
    fun remove(key: String)
}

/** Dependencias de Koin que solo puede construir cada plataforma (p. ej. [SecureStorage]). */
expect val platformModule: Module
