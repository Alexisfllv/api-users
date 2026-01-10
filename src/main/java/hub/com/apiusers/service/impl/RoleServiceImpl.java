package hub.com.apiusers.service.impl;

import hub.com.apiusers.dto.role.RoleDTORequest;
import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.entity.Role;
import hub.com.apiusers.mapper.RoleMapper;
import hub.com.apiusers.service.RoleService;
import hub.com.apiusers.service.domain.RoleServiceDomain;
import hub.com.apiusers.util.page.PageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        log.debug("Role exists: " + roleExist);
        RoleDTOResponse response = roleMapper.toDTOResponse(roleExist);

        return response;
    }

    @Override
    public PageResponse<RoleDTOResponse> pageListRole(int page, int size) {
        log.debug("page/size request : {} , {} ", page , size);
        Pageable pageable = PageRequest.of(page, size);

        Page<Role> rolePage = roleServiceDomain.findAll(pageable);

        return new PageResponse<>(
                rolePage.getContent()
                        .stream()
                        .map(role -> roleMapper.toDTOResponse(role))
                        .toList(),
                rolePage.getNumber(),
                rolePage.getSize(),
                rolePage.getTotalElements(),
                rolePage.getTotalPages()
        );
    }

    @Transactional
    @Override
    public RoleDTOResponse createRole(RoleDTORequest roleDTORequest) {
        Role entity = roleMapper.toRole(roleDTORequest);
        // validate
        roleServiceDomain.roleNameUnique(entity.getName());
        roleServiceDomain.saveRole(entity);
        RoleDTOResponse response = roleMapper.toDTOResponse(entity);
        return response;
    }
}
