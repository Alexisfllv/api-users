package hub.com.apiusers.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;

public record RoleDTOResponse(

        @Schema(description = "Unique role identifier.", example = "1")
        Long id,
        @Schema(description = "Role name.", example = "Admin")
        String name,
        @Schema(description = "Role description.", example = "Supervision complete and Authorizated")
        String description
) {}
