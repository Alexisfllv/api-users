package hub.com.apiusers.service.domain;

import hub.com.apiusers.entity.ExceptionMessages;
import hub.com.apiusers.entity.Role;
import hub.com.apiusers.exception.ResourceNotFoundException;
import hub.com.apiusers.repo.RoleRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
}
