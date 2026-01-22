package hub.com.apiusers.mapper;

import hub.com.apiusers.dto.role.RoleDTORequest;
import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    // toEntity
    Role toRole (RoleDTORequest request);

    // toResponse
    RoleDTOResponse toDTOResponse (Role role);
}
