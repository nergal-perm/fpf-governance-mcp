# Design: DynamoDB Timelog Integration

## Architecture

### 1. AWS SDK Integration
We will add `software.amazon.awssdk:dynamodb` to the `pom.xml`.
*   **Credentials**: We will rely on the `DefaultCredentialsProvider` chain (Env vars, Profile, metadata service). This is standard and secure.
*   **Region**: `AWS_REGION` env var or default.

### 2. Configuration
New Environment Variables:
*   `FPF_TIMELOG_TABLE`: The name of the DynamoDB table.
*   `FPF_TIMELOG_PK`: (Optional) Name of the Partition Key. Default to `id`.

### 3. Tool: `fetch_timelogs`
*   **Signature**: `fetch_timelogs(limit: Int?) -> List<Map<String, Any>>`
*   **Logic**:
    *   Create a DynamoDB `Scan` or `Query` (Scan is likely sufficient for a queue table).
    *   Limit the result.
    *   **Data Transformation**: 
        *   Extract the `id` (String).
        *   Parse the `payload` field, which contains a nested JSON string (with fields like `task`, `role`, `productType`, `startTime`, `endTime`, `duration`, `outcome`).
        *   Flatten the object so the Agent receives a clean, single-level JSON structure for each record, including the `id`.
    *   Return the list.

### 4. Tool: `delete_timelog`
*   **Signature**: `delete_timelog(id: String) -> String`
*   **Logic**:
    *   Accept the `id` of the record to delete.
    *   Execute `DeleteItem` using `id` as the Partition Key.
    *   Return success message.

## Trade-offs
*   **Scan vs Query**: For a "Inbox/Queue" pattern, a full Table Scan with a limit is acceptable as the table is expected to be emptied regularly.
*   **Batch vs Single**: We support fetching a list (Batch). The LLM can decide to process one by one or all at once. This answers the user's question: "fetch them one by one" is less efficient network-wise. We fetch a batch, but the LLM *processes* them one by one in its reasoning loop.

## Security
*   No credentials stored in code.
*   Tools only operate on the configured table.
