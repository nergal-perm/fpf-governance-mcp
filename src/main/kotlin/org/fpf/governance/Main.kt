package org.fpf.governance

import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import org.fpf.governance.protocol.*

fun main() {
    while (true) {
        val line = readlnOrNull() ?: break
        if (line.isBlank()) continue

        try {
            val message = JsonRpcCodec.decode(line)
            handleMessage(message)
        } catch (e: JsonRpcException) {
            // For Parse Error (-32700) or Invalid Request (-32600), id is null.
            println(JsonRpcCodec.encode(JsonRpcResponse(error = e.error, id = null)))
        }
    }
}

fun handleMessage(message: JsonRpcMessage) {
    when (message) {
        is JsonRpcRequest -> {
            if (message.method == "initialize") {
                val response = JsonRpcResponse(
                    id = message.id,
                    result = buildJsonObject {
                        put("protocolVersion", "2024-11-05")
                        putJsonObject("capabilities") {
                            putJsonObject("tools") {}
                            putJsonObject("resources") {}
                        }
                        putJsonObject("serverInfo") {
                            put("name", "fpf-governance")
                            put("version", "0.1.0")
                        }
                    }
                )
                println(JsonRpcCodec.encode(response))
            } else {
                val response = JsonRpcResponse(
                    id = message.id,
                    error = JsonRpcError.METHOD_NOT_FOUND
                )
                println(JsonRpcCodec.encode(response))
            }
        }
        is JsonRpcNotification -> {
            // Notifications are not responded to
        }
        is JsonRpcResponse -> {
            // Responses are ignored by the server for now
        }
    }
}
