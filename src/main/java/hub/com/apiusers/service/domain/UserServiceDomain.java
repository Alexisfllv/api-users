package hub.com.apiusers.service.domain;

import hub.com.apiusers.dto.user.UserDTOResponse;
import hub.com.apiusers.entity.User;
import hub.com.apiusers.exception.ResourceNotFoundException;
import hub.com.apiusers.nums.ExceptionMessages;
import hub.com.apiusers.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserServiceDomain {
    private final UserRepo userRepo;

    // findByIdUser
    public User userExists(Long id){
        return userRepo.findById(id)
                .orElseThrow( () ->
                        new ResourceNotFoundException(ExceptionMessages.RESOURCE_NOT_FOUND_ERROR.message()+id)
                );
    }

}
