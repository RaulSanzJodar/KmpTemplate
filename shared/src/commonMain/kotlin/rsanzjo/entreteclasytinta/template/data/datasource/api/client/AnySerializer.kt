package rsanzjo.entreteclasytinta.template.data.datasource.api.client

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull

/**
 * Serializador para campos `Any` de las respuestas con forma libre. Reproduce lo que hacía Gson:
 * objetos como `Map<String, Any?>`, arrays como `List<Any?>`, números como `Double`, además de
 * `String` y `Boolean`.
 */
object AnySerializer : KSerializer<Any> {

    override val descriptor: SerialDescriptor = JsonElement.serializer().descriptor

    override fun deserialize(decoder: Decoder): Any {
        val element = (decoder as JsonDecoder).decodeJsonElement()
        return element.toAny() ?: ""
    }

    override fun serialize(encoder: Encoder, value: Any) {
        (encoder as JsonEncoder).encodeJsonElement(value.toJsonElement())
    }
}

fun JsonElement.toAny(): Any? = when (this) {
    is JsonNull -> null
    is JsonPrimitive -> when {
        isString -> content
        else -> booleanOrNull ?: doubleOrNull ?: content
    }
    is JsonObject -> mapValues { (_, value) -> value.toAny() }
    is JsonArray -> map { it.toAny() }
}

private fun Any?.toJsonElement(): JsonElement = when (this) {
    null -> JsonNull
    is JsonElement -> this
    is String -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
    is Number -> JsonPrimitive(this)
    is Map<*, *> -> JsonObject(entries.associate { (key, value) -> key.toString() to value.toJsonElement() })
    is Iterable<*> -> JsonArray(map { it.toJsonElement() })
    else -> JsonPrimitive(toString())
}
