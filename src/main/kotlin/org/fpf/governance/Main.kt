package org.fpf.governance

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import org.fpf.governance.protocol.*

// 1. Tool Interface
interface McpTool {
    val name: String
    val description: String
    val inputSchema: JsonObject
    fun call(
            args: JsonObject
    ): JsonElement // Updated to use JsonObject for args and JsonElement for result
}

// 2. Tool Registry
object ToolRegistry {
    private val tools = mutableMapOf<String, McpTool>()

    fun register(tool: McpTool) {
        tools[tool.name] = tool
    }

    fun listTools(): List<JsonObject> {
        return tools.values.map { tool ->
            buildJsonObject {
                put("name", tool.name)
                put("description", tool.description)
                put("inputSchema", tool.inputSchema)
            }
        }
    }

    fun callTool(name: String, args: JsonObject): JsonElement {
        val tool = tools[name] ?: throw IllegalArgumentException("Tool not found: $name")
        return tool.call(args)
    }
}

// 3. Echo Tool Implementation
class EchoTool : McpTool {
    override val name = "echo"
    override val description = "Echoes back the input message"
    // Using simple string parsing for schema for now to avoid complexity constructing JsonObject
    // manually if unnecessary
    override val inputSchema = buildJsonObject {
        put("type", "object")
        putJsonObject("properties") { putJsonObject("message") { put("type", "string") } }
        put(
                "required",
                kotlinx.serialization.json.buildJsonArray {
                    add(kotlinx.serialization.json.JsonPrimitive("message"))
                }
        )
    }

    override fun call(args: JsonObject): JsonElement {
        val message =
                (args["message"] as? kotlinx.serialization.json.JsonPrimitive)?.content
                        ?: "No message provided"
        return buildJsonObject {
            put(
                    "content",
                    kotlinx.serialization.json.buildJsonArray {
                        add(
                                buildJsonObject {
                                    put("type", "text")
                                    put("text", message)
                                }
                        )
                    }
            )
        }
    }
}

fun main() {
    // Register tools
    ToolRegistry.register(EchoTool())

    while (true) {
        val line = readlnOrNull() ?: break
        if (line.isBlank()) continue

        try {
            val message = JsonRpcCodec.decode(line)
            handleMessage(message)
        } catch (e: JsonRpcException) {
            println(JsonRpcCodec.encode(JsonRpcResponse(error = e.error, id = null)))
        }
    }
}

fun handleMessage(message: JsonRpcMessage) {
    when (message) {
        is JsonRpcRequest -> {
            try {
                when (message.method) {
                    "initialize" -> {
                        val response =
                                JsonRpcResponse(
                                        id = message.id,
                                        result =
                                                buildJsonObject {
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
                    }
                    "tools/list" -> {
                        val toolsList = ToolRegistry.listTools()
                        val result = buildJsonObject {
                            put(
                                    "tools",
                                    kotlinx.serialization.json.buildJsonArray {
                                        toolsList.forEach { tool -> add(tool) }
                                    }
                            )
                        }
                        val response = JsonRpcResponse(id = message.id, result = result)
                        println(JsonRpcCodec.encode(response))
                    }
                    "tools/call" -> {
                        // Extract params. Tool execution expects 'name' and 'arguments' in params
                        val params = message.params as? JsonObject
                        val toolName =
                                (params?.get("name") as? kotlinx.serialization.json.JsonPrimitive)
                                        ?.content
                        val args = params?.get("arguments") as? JsonObject

                        if (toolName != null && args != null) {
                            val result = ToolRegistry.callTool(toolName, args)
                            val response = JsonRpcResponse(id = message.id, result = result)
                            println(JsonRpcCodec.encode(response))
                        } else {
                            throw JsonRpcException(JsonRpcError.INVALID_PARAMS)
                        }
                    }
                    else -> {
                        val response =
                                JsonRpcResponse(
                                        id = message.id,
                                        error = JsonRpcError.METHOD_NOT_FOUND
                                )
                        println(JsonRpcCodec.encode(response))
                    }
                }
            } catch (e: Exception) {
                val error = if (e is JsonRpcException) e.error else JsonRpcError.INTERNAL_ERROR
                val response = JsonRpcResponse(id = message.id, error = error)
                println(JsonRpcCodec.encode(response))
            }
        }
        is JsonRpcNotification -> {}
        is JsonRpcResponse -> {}
    }
}
