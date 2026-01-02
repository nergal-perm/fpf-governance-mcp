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

// 4. Check Rule Infrastructure
data class ValidationResult(val valid: Boolean, val errors: List<String>)

interface CheckRule {
    fun validate(path: String, content: String): ValidationResult
}

// 5. Rule Implementation: RULE-001 Location
class Rule001Location : CheckRule {
    override fun validate(path: String, content: String): ValidationResult {
        // Simple frontmatter parsing using Regex
        val typeRegex = Regex("^type:\\s*(\\w+)", RegexOption.MULTILINE)
        val match = typeRegex.find(content)
        val type = match?.groupValues?.get(1)

        if (type == null) {
            // If no type is found, we assume it's not subject to this rule (or maybe a different
            // error, but let's be lenient for stub)
            return ValidationResult(true, emptyList())
        }

        val expectedDir =
                when (type) {
                    "dissatisfaction" -> "20_Registry/Problems"
                    "hypothesis" -> "30_Laboratory/Hypotheses"
                    "decision" -> "40_Governance/Decisions"
                    "project" -> "50_Execution/Projects"
                    else -> null // Unknown type, no location rule enforced by this specific rule
                }

        if (expectedDir != null) {
            if (!path.contains(expectedDir)) {
                return ValidationResult(
                        false,
                        listOf(
                                "RULE-001: Artifact of type '$type' must be located in '$expectedDir'. Current path: '$path'"
                        )
                )
            }
        }
        return ValidationResult(true, emptyList())
    }
}

// 6. Check Registry
object CheckRegistry {
    private val rules = mutableListOf<CheckRule>()

    init {
        // Check "registry" logic: Initialize with hardcoded rule
        register(Rule001Location())
    }

    fun register(rule: CheckRule) {
        rules.add(rule)
    }

    fun validate(path: String, content: String): ValidationResult {
        val allErrors = mutableListOf<String>()
        for (rule in rules) {
            val result = rule.validate(path, content)
            if (!result.valid) {
                allErrors.addAll(result.errors)
            }
        }
        return ValidationResult(allErrors.isEmpty(), allErrors)
    }
}

// 7. Check File Tool
class CheckFileTool : McpTool {
    override val name = "validate_governance_compliance"
    override val description =
            "FPF Governance Linter. REQUIRED: Call this tool to validate content and paths for any new or modified artifact (Hypothesis, Decision, etc.) BEFORE writing to disk to ensure compliance."
    override val inputSchema = buildJsonObject {
        put("type", "object")
        putJsonObject("properties") {
            putJsonObject("path") { put("type", "string") }
            putJsonObject("content") { put("type", "string") }
        }
        put(
                "required",
                kotlinx.serialization.json.buildJsonArray {
                    add(kotlinx.serialization.json.JsonPrimitive("path"))
                    add(kotlinx.serialization.json.JsonPrimitive("content"))
                }
        )
    }

    override fun call(args: JsonObject): JsonElement {
        val path =
                (args["path"] as? kotlinx.serialization.json.JsonPrimitive)?.content
                        ?: throw IllegalArgumentException("Missing path")
        val content =
                (args["content"] as? kotlinx.serialization.json.JsonPrimitive)?.content
                        ?: throw IllegalArgumentException("Missing content")

        val result = CheckRegistry.validate(path, content)

        return buildJsonObject {
            put(
                    "content",
                    kotlinx.serialization.json.buildJsonArray {
                        add(
                                buildJsonObject {
                                    put("type", "text")
                                    put(
                                            "text",
                                            if (result.valid) "Valid"
                                            else "Invalid: ${result.errors.joinToString("; ")}"
                                    )
                                }
                        )
                    }
            )
            put("isError", !result.valid) // Optional hint if client supports it
        }
    }
}

fun main() {
    // Register tools
    ToolRegistry.register(EchoTool())
    ToolRegistry.register(CheckFileTool())

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
