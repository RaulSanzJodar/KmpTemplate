package rsanzjo.entreteclasytinta.template.testing

import rsanzjo.entreteclasytinta.template.common.platform.SecureStorage

/** [SecureStorage] en memoria para los tests. */
class FakeSecureStorage : SecureStorage {
    private val values = mutableMapOf<String, String>()

    override fun getString(key: String): String? = values[key]
    override fun putString(key: String, value: String) { values[key] = value }
    override fun remove(key: String) { values.remove(key) }
}
