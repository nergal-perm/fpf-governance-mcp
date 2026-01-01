package org.fpf.governance

fun main() {
    while (true) {
        val line = readlnOrNull() ?: break
        if (line.isBlank()) continue

        // Basic check for initialize method
        if (line.contains("\"method\": \"initialize\"") ||
                        line.contains("\"method\":\"initialize\"")
        ) {
            // Extract ID simply
            val idRegex = "\"id\"\\s*:\\s*(\\d+|null|\"[^\"]+\")".toRegex()
            val match = idRegex.find(line)
            val rawId = match?.groupValues?.get(1) ?: "1"

            // Construct mocked response
            val response =
                    """
            {
              "jsonrpc": "2.0",
              "id": $rawId,
              "result": {
                "protocolVersion": "2024-11-05",
                "capabilities": {
                    "tools": {},
                    "resources": {}
                },
                "serverInfo": {
                  "name": "fpf-governance",
                  "version": "0.1.0"
                }
              }
            }
            """.trimIndent()

            println(response)
        }
    }
}
