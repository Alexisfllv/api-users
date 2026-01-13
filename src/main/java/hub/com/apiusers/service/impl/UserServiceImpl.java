package hub.com.apiusers.service.impl;

import hub.com.apiusers.dto.user.UserDTOResponse;
import hub.com.apiusers.entity.User;
import hub.com.apiusers.mapper.UserMapper;
import hub.com.apiusers.service.UserService;
import hub.com.apiusers.service.domain.UserServiceDomain;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
