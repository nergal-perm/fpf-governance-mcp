package org.fpf.governance.web.dto;

import java.util.List;

public record DeconstructResponse(
    List<Holon> holons,
    String reframedProblem,
    List<CategoryError> categoryErrors,
    List<GhostReinstantiation> ghostReinstantiations
) {}
