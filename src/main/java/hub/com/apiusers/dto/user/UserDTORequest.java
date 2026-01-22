package hub.com.apiusers.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Set;

public record UserDTORequest(

        @Schema(description = "User name", example = "Alex", required = true)
        @NotBlank(message = "{field.required}")
        @Size(min = 2, max = 50, message = "{field.size.range}")
        String username,

        @Schema(description = "User password", example = "1234", required = true)
        @NotBlank(message = "{field.required}")
        @Size(min = 6, max = 255, message = "{field.size.range}")
        String password,

        @Schema(description = "User full name", example = "Alex Lauv", required = true)
        @NotBlank(message = "{field.required}")
        @Size(min = 2, max = 100, message = "{field.size.range}")
        String fullName,

        @Schema(description = "User email", example = "Alex-Lauv@gmail.com", required = true)
        @NotBlank(message = "{field.required}")
        @Size(min = 5, max = 100, message = "{field.size.range}")
        @Email(message = "{field.email.invalid}")
        String email,

        @Schema(description = "User Role_Id", example = "1", required = true)
        @NotNull(message = "{field.required}")
        Set<@Positive(message = "{field.must.be.positive}") Long> roles
) {
}