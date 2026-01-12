# HTMX Integration Design

## Context
The "Board Room" web application requires a dynamic interface for logic deconstruction and plan visualization. HTMX provides a way to achieve this without the overhead of a full SPA framework.

## Decisions

### 1. Library Selection: HTMX
- **Why**: Allows declarative partial page updates via standard HTML attributes (`hx-get`, `hx-post`, etc.). Reduces JavaScript boilerplate.

### 2. Server-Side Rendering: Spring Boot + Thymeleaf
- **Why**: HTMX relies on the server returning HTML fragments. Spring Boot with Thymeleaf is the standard solution for server-side rendering in the Java ecosystem.

### 3. Integration with HTTP Server
- The HTTP server must be able to serve static assets (htmx.min.js) and handle requests that expect HTML fragments instead of full pages or JSON.

## Interaction Patterns
- **Status Check**: Instead of checking DB connection on startup (which slows down boot and clutters logs), we will expose an on-demand check via the UI. A "Check Connection" button will trigger an `hx-get` request, and the server will return a `<span>` with the status (Success/Fail), replacing the button.