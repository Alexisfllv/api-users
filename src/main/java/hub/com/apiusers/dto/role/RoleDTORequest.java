package hub.com.apiusers.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleDTORequest(

        @Schema(description = "Role name.", example = "Admin", required = true)
        @NotBlank(message = "{field.required}")
        @Size(min = 2, max = 50, message = "{field.size.range}")
        String name,

        @Schema(description = "Role descrption.", example = "Supervision complete and Authorizated", required = true)
        @NotBlank(message = "{field.required}")
        @Size(min = 2, max = 200, message = "{field.size.range}")
        String description
) {}
