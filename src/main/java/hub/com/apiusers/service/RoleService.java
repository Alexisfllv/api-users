package hub.com.apiusers.service;

import hub.com.apiusers.dto.role.RoleDTORequest;
import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.util.page.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface RoleService {

    // GET

    // findByIdRole
    RoleDTOResponse findByIdRole (Long id);

    // page list roles
    PageResponse<RoleDTOResponse> pageListRole(int page, int size);

    // POST

    // createRole
    RoleDTOResponse createRole(RoleDTORequest roleDTORequest);

    // importRole
    List<RoleDTOResponse> importRolesFromCsv(MultipartFile file) throws IOException;


    // PUT
    // updateRole
    RoleDTOResponse updateRole(RoleDTORequest roleDTORequest, Long id);

    // DELETE

    // deleteByIdRole
    void deleteRole(Long id);

}
