package org.fpf.governance.web.dto;

public record CategoryError(
    String description,
    String violation,
    String correction
) {}
