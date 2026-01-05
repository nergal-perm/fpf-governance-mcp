# Implement DynamoDB Timelog Tool

## Goal
Add MCP tools to interact with an AWS DynamoDB table to fetch and manage "timelog" records. This enables the LLM to retrieve raw time-tracking data, process it (by creating artifacts in the user's vault), and remove the processed records from the database.

## Requirements
1.  **AWS Integration**: Integrate AWS SDK for Java (DynamoDB) into the project.
2.  **Configuration**: Support configuration for AWS Table Name and Region via environment variables.
3.  **Fetch Tool**: `fetch_timelogs` tool to retrieve pending records.
    *   Support limiting the number of records.
    *   Return records as structured JSON "observations".
4.  **Delete Tool**: `delete_timelog` tool to remove a record by its Primary Key.
    *   This supports the "process then delete" workflow.

## Motivation
The user wants to bridge their time-tracking system (stored in DynamoDB) with their FPF Knowledge Base. The LLM acts as the intelligent processor: reading raw logs, contextualizing them into "Project" or "Work" artifacts, and cleaning up the queue.

## Questions/Clarifications
*   **Schema**: We assume a generic schema or a simple Partition Key (e.g., `id`). The `delete` tool will require the explicit key(s) returned by the `fetch` tool.
*   **Workflow**: We chose the "Passive Tool" approach (LLM calls Fetch -> LLM processes -> LLM calls Delete) rather than Server-Sent Events Sampling, as we are currently using the `stdio` transport and haven't migrated to the HTTP/Spring architecture yet. This is compatible with all MCP clients.
