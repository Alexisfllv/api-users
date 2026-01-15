package hub.com.apiusers.service.impl;

import hub.com.apiusers.dto.user.UserDTORequest;
import hub.com.apiusers.dto.user.UserDTORequestUpdate;
import hub.com.apiusers.dto.user.UserDTOResponse;
import hub.com.apiusers.entity.Role;
import hub.com.apiusers.entity.User;
import hub.com.apiusers.mapper.UserMapper;
import hub.com.apiusers.service.UserService;
import hub.com.apiusers.service.domain.UserServiceDomain;
import hub.com.apiusers.util.page.PageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserServiceDomain userServiceDomain;
    private final UserMapper userMapper;

    // GET

    @Transactional
    @Override
    public UserDTOResponse findByIdUser(Long id) {
        log.warn("ID : "+id);
        User userExist = userServiceDomain.userExists(id);

        log.warn("UserExist : "+userExist.toString());
        UserDTOResponse response = userMapper.toUserDTOResponse(userExist);
        log.warn("UserDTOResponse : "+response.toString());
        return response;
    }

    @Transactional
    @Override
    public PageResponse<UserDTOResponse> pageListUser(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> pageUser = userServiceDomain.findAllPage(pageable);

        return new PageResponse<>(
                pageUser.getContent()
                        .stream()
                        .map(user -> userMapper.toUserDTOResponse(user))
                        .toList(),
                pageUser.getNumber(),
                pageUser.getSize(),
                pageUser.getTotalElements(),
                pageUser.getTotalPages()
        );

    }

    @Transactional
    @Override
    public UserDTOResponse createUser(UserDTORequest request) {
        User user = userMapper.toUser(request);

        log.warn("UserDTORequest : "+user.toString());
        // validate unique
        userServiceDomain.validateUniqueUsername(request.username());
        userServiceDomain.validateUniqueEmail(request.email());

        // set user
        Set<Role> rolesExist = userServiceDomain.validateRoleExists(request.roles());

        // save

        user.setRoles(rolesExist);
        user.setActive(true);

        User saved = userServiceDomain.saveUser(user);
        return userMapper.toUserDTOResponse(saved);
    }

    @Transactional
    @Override
    public UserDTOResponse updateUser(Long id, UserDTORequestUpdate userDTORequestUpdate) {
        // validate exist user
        User userExist =  userServiceDomain.userExists(id);

        userServiceDomain.validateUniqueUsername(userDTORequestUpdate.username());
        userServiceDomain.validateUniqueEmail(userDTORequestUpdate.email());
        Set<Role> rolesExist = userServiceDomain.validateRoleExists(userDTORequestUpdate.roles());

        userExist.setUsername(userDTORequestUpdate.username());
        userExist.setPassword(userDTORequestUpdate.password());
        userExist.setFullName(userDTORequestUpdate.fullName());
        userExist.setEmail(userDTORequestUpdate.email());
        userExist.setActive(userDTORequestUpdate.active());
        userExist.setRoles(rolesExist);

        User saved = userServiceDomain.saveUser(userExist);
        return userMapper.toUserDTOResponse(saved);
    }

    @Override
    public void deleteUser(Long id) {
        User userExist = userServiceDomain.userExists(id);
        userServiceDomain.deleteUser(userExist);
    }
}
