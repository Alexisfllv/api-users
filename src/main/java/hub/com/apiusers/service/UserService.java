package hub.com.apiusers.service;

import hub.com.apiusers.dto.user.UserDTOResponse;

public interface UserService {
    // GET

    // findByIdUser
    UserDTOResponse findByIdUser(Long id);

}
