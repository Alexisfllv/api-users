package hub.com.apiusers.service;

import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.util.page.PageResponse;


public interface RoleService {

    // GET

    // findByIdRole
    RoleDTOResponse findByIdRole (Long id);

    // page list roles
    PageResponse<RoleDTOResponse> pageListRole(int page, int size);


}
