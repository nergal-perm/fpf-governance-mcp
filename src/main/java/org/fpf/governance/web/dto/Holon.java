package org.fpf.governance.web.dto;

public record Holon(
    String id,
    String name,
    String type,
    String role,
    String manifestation,
    String parentID
) {}
