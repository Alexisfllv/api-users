package hub.com.apiusers.service.domain;

import hub.com.apiusers.dto.user.UserDTOResponse;
import hub.com.apiusers.entity.Role;
import hub.com.apiusers.entity.User;
import hub.com.apiusers.exception.ResourceNotFoundException;
import hub.com.apiusers.exception.UniqueException;
import hub.com.apiusers.nums.ExceptionMessages;
import hub.com.apiusers.repo.RoleRepo;
import hub.com.apiusers.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserServiceDomain {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    // findByIdUser
    public User userExists(Long id){
        return userRepo.findById(id)
                .orElseThrow( () ->
                        new ResourceNotFoundException(ExceptionMessages.RESOURCE_NOT_FOUND_ERROR.message()+id)
                );
    }

    // user page findAll
    public Page<User> findAllPage(Pageable pageable){
        return userRepo.findAll(pageable);
    }

    // validate unique username
    public void validateUniqueUsername(String username){
        if (userRepo.existsByUsername(username)){
            throw new UniqueException(
                    ExceptionMessages.UNIQUE_EXC.message()+username
            );
        }
    }

    // validate unique email
    public void validateUniqueEmail(String email){
        if (userRepo.existsByEmail(email)){
            throw new UniqueException(
                    ExceptionMessages.UNIQUE_EXC.message()+email
            );
        }
    }


    // validate roles Exist
    public Set<Role> validateRoleExists (Set<Long> roleIds){
        // verificar si un rol no existe
        Set<Role> found = roleRepo.findAllById(roleIds)
                .stream()
                .collect(Collectors.toSet());
        // comparar
        if (found.size()!=roleIds.size()){
            throw new ResourceNotFoundException(ExceptionMessages.RESOURCE_NOT_FOUND_ERROR.message()+roleIds);
        }
        return found;
    }

    // saved
    public User saveUser(User user){
        return userRepo.save(user);
    }

    // delete
    public void deleteUser(User user){
        userRepo.delete(user);
    }

}
