package rsanzjo.entreteclasytinta.template.common.platform

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import rsanzjo.entreteclasytinta.template.common.Log
import java.io.IOException
import java.security.GeneralSecurityException

actual val platformModule: Module = module {
    single<SecureStorage> { AndroidSecureStorage(androidContext()) }
}

private class AndroidSecureStorage(context: Context) : SecureStorage {

    private val prefs: SharedPreferences = try {
        createPrefs(context)
    } catch (e: GeneralSecurityException) {
        resetAndCreatePrefs(context, e)
    } catch (e: IOException) {
        resetAndCreatePrefs(context, e)
    }

    private fun createPrefs(context: Context): SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /**
     * El fichero no se puede descifrar con la clave del Keystore (p. ej. restaurado de una copia de
     * otro dispositivo): se descarta, y con él la sesión, en lugar de dejar la app sin arrancar.
     */
    private fun resetAndCreatePrefs(context: Context, cause: Exception): SharedPreferences {
        Log.w("SecureStorage", "Encrypted preferences are unreadable and will be reset: ${cause.message}")
        context.deleteSharedPreferences(PREFS_NAME)
        return createPrefs(context)
    }

    override fun getString(key: String): String? = prefs.getString(key, null)

    override fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }

    private companion object {
        // Excluido de las copias de seguridad en res/xml/backup_rules.xml y data_extraction_rules.xml.
        const val PREFS_NAME = "encrypted_token_prefs"
    }
}
