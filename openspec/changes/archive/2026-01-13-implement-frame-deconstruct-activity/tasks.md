# Implementation Tasks

- [x] **Create Prompt Template**: Create a resource file (`src/main/resources/prompts/deconstruct.txt`) containing the enhanced prompt.
- [x] **Create FrameActivityController**: Create a controller to handle the "Frame & Deconstruct" logic. It will load the prompt, inject user input, and call the `Brain`.
- [x] **Update UI Layout**: Create `activity_frame.html` to implement the 2-column layout.
- [x] **Add Markdown Rendering**: Ensure the right column renders Markdown using a client-side library.
- [x] **Define Response DTOs**: Create Java classes to map the JSON structure (`DeconstructResponse`, `Holon`, `CategoryError`, `GhostReinstantiation`).
- [x] **Implement JSON Extraction & Parsing**: Update `FrameActivityController` to extract JSON from the brain response and deserialize it into the DTOs.
- [x] **Implement Structured UI**: Update `activity_frame.html` to render the Holons Table, Error Callouts, and Warning Callouts using Thymeleaf.
- [x] **Integration Test**: Verify the end-to-end flow, including JSON extraction and UI rendering.
