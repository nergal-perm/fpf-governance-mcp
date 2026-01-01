# Tasks

- [x] Add `kotlinx-serialization-json` dependency to `pom.xml`. <!-- type: chore -->
- [x] Create `org.fpf.governance.protocol` package and define JSON-RPC 2.0 data models (`Request`, `Response`, `Notification`, `Error`). <!-- type: feat -->
- [x] Implement `JsonRpcCodec` to parse strings into `JsonRpcMessage` objects and serialize responses, including error handling for malformed JSON. <!-- type: feat -->
- [x] Update `Main.kt` to use the new data models and codec for the `initialize` handshake loop. <!-- type: refactor -->
- [x] Add unit tests for `JsonRpcCodec` covering valid requests, responses, and standard error cases (Parse Error, Invalid Request). <!-- type: test -->
