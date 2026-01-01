# Proposal: Implement Type-Safe JSON-RPC Model

## Summary
Replace the current string-based mock implementation with a robust, type-safe JSON-RPC 2.0 handling layer using `kotlinx.serialization`.

## Problem
The current implementation uses brittle string matching and manual JSON string construction. This is error-prone, hard to maintain, and does not support the full JSON-RPC 2.0 specification (e.g., error handling, proper parsing).

## Solution
1.  Add `kotlinx-serialization-json` dependency.
2.  Define Kotlin data classes representing JSON-RPC 2.0 messages (Request, Response, Notification, Error).
3.  Implement a robust deserialization logic to handle incoming messages.
4.  Refactor the main loop to use this object model.
