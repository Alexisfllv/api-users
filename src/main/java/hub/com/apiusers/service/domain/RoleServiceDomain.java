package hub.com.apiusers.service.domain;

import hub.com.apiusers.exception.UniqueException;
import hub.com.apiusers.nums.ExceptionMessages;
import hub.com.apiusers.entity.Role;
import hub.com.apiusers.exception.ResourceNotFoundException;
import hub.com.apiusers.repo.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleServiceDomain {

    private final RoleRepo roleRepo;


    // role exist
    public Role roleExists(Long id){
        return roleRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ExceptionMessages.RESOURCE_NOT_FOUND_ERROR.message()+id));
    }

    // role findAll
    public Page<Role> findAll(Pageable pageable){
        return roleRepo.findAll(pageable);
    }

    // validate unique
    public void roleNameUnique(String name){
        if (roleRepo.existsByName(name)) {
            throw new UniqueException
                    (ExceptionMessages.UNIQUE_EXC.message()+name);
        }
    }

    // role save
    public Role  saveRole(Role role){
        return roleRepo.save(role);
    }
}
