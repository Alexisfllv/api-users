package hub.com.apiusers.dto.user;

import hub.com.apiusers.dto.role.RoleDTOResponse;

import java.util.Set;

public record UserDTOResponse(
        Long id,
        String username,
        String fullName,
        String email,
        Boolean active,
        Set<RoleDTOResponse> roles
) { }