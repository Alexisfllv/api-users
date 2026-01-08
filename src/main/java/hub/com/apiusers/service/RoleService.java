package hub.com.apiusers.service;

import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.entity.Role;

public interface RoleService {

    // GET

    // findByIdRole
    RoleDTOResponse findByIdRole (Long id);


}
