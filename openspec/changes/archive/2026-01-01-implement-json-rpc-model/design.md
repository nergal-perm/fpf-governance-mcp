# Design: JSON-RPC Layer

## Data Model
We will use Kotlin's `sealed interface` or `sealed class` hierarchy to model JSON-RPC messages to ensure type safety and exhaustiveness in handling.

### Structure
- `JsonRpcMessage`: Top-level marker.
- `JsonRpcRequest`: Represents a method call with an ID.
- `JsonRpcNotification`: Represents a method call without an ID.
- `JsonRpcResponse`: Represents a successful result or an error response.
- `JsonRpcError`: Structure for the error object within a response.

### ID Handling
JSON-RPC 2.0 allows IDs to be strings, numbers, or null. We will model this using `kotlinx.serialization.json.JsonElement` or a custom wrapper to preserve exact type fidelity.

## Serialization
We will use `kotlinx.serialization` for JSON handling.
- **Why**: It is Kotlin-first, multiplatform-ready, and works well with GraalVM Native Image (reduced reflection usage compared to Jackson).

## Error Handling
The parser must gracefully handle:
1.  **Malformed JSON**: Returns Parse Error (-32700).
2.  **Invalid Request**: Returns Invalid Request (-32600) if the structure doesn't match a valid RPC object.
3.  **Method Not Found**: Returns (-32601) when dispatching.

## Architecture Update
The `Main` loop will be refactored to:
1.  Read line.
2.  Pass line to a `JsonRpcCodec` (new component).
3.  `JsonRpcCodec` returns a `JsonRpcMessage` or throws/returns error.
4.  `Main` (or a `Dispatcher`) acts on the message.
