package hub.com.apiusers.service;

import hub.com.apiusers.dto.user.UserDTORequest;
import hub.com.apiusers.dto.user.UserDTORequestUpdate;
import hub.com.apiusers.dto.user.UserDTOResponse;
import hub.com.apiusers.util.page.PageResponse;
import org.springframework.data.domain.PageRequest;

public interface UserService {
    // GET

    // findByIdUser
    UserDTOResponse findByIdUser(Long id);

    // pageListUser
    PageResponse<UserDTOResponse> pageListUser(int page, int size);

    // POST

    // createUser
    UserDTOResponse createUser(UserDTORequest userDTORequest);

    // PUT

    // updateUser
    UserDTOResponse updateUser(Long id, UserDTORequestUpdate userDTORequestUpdate);

}
