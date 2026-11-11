package rsanzjo.entreteclasytinta.template.data.datasource.supabase

import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import rsanzjo.entreteclasytinta.template.common.Log
import rsanzjo.entreteclasytinta.template.common.platform.SecureStorage

/**
 * Guarda la sesión de Supabase en el [SecureStorage] cifrado de la plataforma, en lugar del
 * almacén en claro que usa supabase-kt por defecto.
 */
internal class SecureSessionManager(private val storage: SecureStorage) : SessionManager {

    override suspend fun saveSession(session: UserSession) {
        storage.putString(SESSION_KEY, json.encodeToString(session))
    }

    override suspend fun loadSession(): UserSession = loadSessionOrNull() ?: error("No session stored")

    override suspend fun loadSessionOrNull(): UserSession? {
        val stored = storage.getString(SESSION_KEY) ?: return null
        return try {
            json.decodeFromString<UserSession>(stored)
        } catch (e: SerializationException) {
            // Formato de otra versión de la librería: se descarta y el usuario vuelve a iniciar sesión.
            Log.w("SecureSessionManager", "Stored session could not be decoded: ${e.message}")
            storage.remove(SESSION_KEY)
            null
        }
    }

    override suspend fun deleteSession() {
        storage.remove(SESSION_KEY)
    }

    private companion object {
        const val SESSION_KEY = "supabase_session"

        // `encodeDefaults`: sin él se pierde `expiresAt` y la sesión restaurada parecería recién creada.
        val json = Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
        }
    }
}
