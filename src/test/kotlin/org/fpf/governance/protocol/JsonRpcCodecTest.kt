package org.fpf.governance.protocol

import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class JsonRpcCodecTest {

    @Test
    fun `decode valid request`() {
        val input = """{"jsonrpc": "2.0", "method": "test", "params": [1], "id": 1}"""
        val message = JsonRpcCodec.decode(input)
        assertTrue(message is JsonRpcRequest)
        assertEquals("test", message.method)
        assertEquals(JsonPrimitive(1), message.id)
    }

    @Test
    fun `decode valid notification`() {
        val input = """{"jsonrpc": "2.0", "method": "notify"}"""
        val message = JsonRpcCodec.decode(input)
        assertTrue(message is JsonRpcNotification)
        assertEquals("notify", message.method)
    }

    @Test
    fun `decode parse error`() {
        val input = """{"jsonrpc": "2.0", "method": "test"""
        val exception = assertFailsWith<JsonRpcException> {
            JsonRpcCodec.decode(input)
        }
        assertEquals(JsonRpcError.PARSE_ERROR.code, exception.error.code)
    }

    @Test
    fun `decode invalid request - empty object`() {
        val input = """{}"""
        val exception = assertFailsWith<JsonRpcException> {
            JsonRpcCodec.decode(input)
        }
        assertEquals(JsonRpcError.INVALID_REQUEST.code, exception.error.code)
    }

    @Test
    fun `encode response`() {
        val response = JsonRpcResponse(
            id = JsonPrimitive(1),
            result = JsonPrimitive("success")
        )
        val encoded = JsonRpcCodec.encode(response)
        assertTrue(encoded.contains(""""jsonrpc":"2.0""""))
        assertTrue(encoded.contains(""""id":1"""))
        assertTrue(encoded.contains(""""result":"success""""))
    }
}
