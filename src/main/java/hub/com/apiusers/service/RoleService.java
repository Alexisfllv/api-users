package hub.com.apiusers.service;

import hub.com.apiusers.dto.role.RoleDTORequest;
import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.util.page.PageResponse;


public interface RoleService {

    // GET

    // findByIdRole
    RoleDTOResponse findByIdRole (Long id);

    // page list roles
    PageResponse<RoleDTOResponse> pageListRole(int page, int size);

    // POST

    // createRole
    RoleDTOResponse createRole(RoleDTORequest roleDTORequest);

    // PUT
    // updateRole
    RoleDTOResponse updateRole(RoleDTORequest roleDTORequest, Long id);

}
