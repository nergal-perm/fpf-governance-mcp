package org.fpf.governance.protocol

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(with = JsonRpcMessageSerializer::class)
sealed interface JsonRpcMessage {
    val jsonrpc: String
}

object JsonRpcMessageSerializer : KSerializer<JsonRpcMessage> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("JsonRpcMessage")

    override fun deserialize(decoder: Decoder): JsonRpcMessage {
        val input = decoder as? JsonDecoder ?: throw SerializationException("Expected JsonDecoder")
        val element = input.decodeJsonElement()
        val jsonObject = element.jsonObject

        val serializer = when {
            "method" in jsonObject -> {
                if ("id" in jsonObject) JsonRpcRequest.serializer() else JsonRpcNotification.serializer()
            }
            "result" in jsonObject || "error" in jsonObject -> JsonRpcResponse.serializer()
            else -> JsonRpcRequest.serializer()
        }

        return input.json.decodeFromJsonElement(serializer, element)
    }

    override fun serialize(encoder: Encoder, value: JsonRpcMessage) {
        when (value) {
            is JsonRpcRequest -> encoder.encodeSerializableValue(JsonRpcRequest.serializer(), value)
            is JsonRpcNotification -> encoder.encodeSerializableValue(JsonRpcNotification.serializer(), value)
            is JsonRpcResponse -> encoder.encodeSerializableValue(JsonRpcResponse.serializer(), value)
        }
    }
}

@Serializable
data class JsonRpcRequest(
    override val jsonrpc: String = "2.0",
    val method: String,
    val params: JsonElement? = null,
    val id: JsonElement
) : JsonRpcMessage

@Serializable
data class JsonRpcNotification(
    override val jsonrpc: String = "2.0",
    val method: String,
    val params: JsonElement? = null
) : JsonRpcMessage

@Serializable
data class JsonRpcResponse(
    override val jsonrpc: String = "2.0",
    val result: JsonElement? = null,
    val error: JsonRpcError? = null,
    val id: JsonElement? = null
) : JsonRpcMessage

@Serializable
data class JsonRpcError(
    val code: Int,
    val message: String,
    val data: JsonElement? = null
) {
    companion object {
        val PARSE_ERROR = JsonRpcError(-32700, "Parse error")
        val INVALID_REQUEST = JsonRpcError(-32600, "Invalid Request")
        val METHOD_NOT_FOUND = JsonRpcError(-32601, "Method not found")
        val INVALID_PARAMS = JsonRpcError(-32602, "Invalid params")
        val INTERNAL_ERROR = JsonRpcError(-32603, "Internal error")
    }
}

class JsonRpcException(val error: JsonRpcError) : Exception(error.message)