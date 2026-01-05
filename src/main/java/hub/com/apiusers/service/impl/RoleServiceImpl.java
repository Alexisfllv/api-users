package hub.com.apiusers.service.impl;

import hub.com.apiusers.dto.role.RoleDTORequest;
import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.entity.Role;
import hub.com.apiusers.mapper.RoleMapper;
import hub.com.apiusers.service.RoleService;
import hub.com.apiusers.service.domain.RoleServiceDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final RoleServiceDomain roleServiceDomain;

    // GET
    @Override
    public RoleDTOResponse findByIdRole(Long id) {
        Role roleExist = roleServiceDomain.roleExists(id);

        log.info("Role exists: " + roleExist);
        RoleDTOResponse response = roleMapper.toDTOResponse(roleExist);

        return response;
    }
}
