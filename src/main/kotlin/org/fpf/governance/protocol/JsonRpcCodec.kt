package org.fpf.governance.protocol

import kotlinx.serialization.json.Json

object JsonRpcCodec {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        explicitNulls = false
    }

    fun decode(string: String): JsonRpcMessage {
        val element = try {
            json.parseToJsonElement(string)
        } catch (e: Exception) {
            throw JsonRpcException(JsonRpcError.PARSE_ERROR)
        }

        return try {
            json.decodeFromJsonElement(JsonRpcMessage.serializer(), element)
        } catch (e: Exception) {
            throw JsonRpcException(JsonRpcError.INVALID_REQUEST)
        }
    }

    fun encode(message: JsonRpcMessage): String {
        return json.encodeToString(JsonRpcMessage.serializer(), message)
    }
}
