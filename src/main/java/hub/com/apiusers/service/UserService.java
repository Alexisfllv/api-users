package hub.com.apiusers.service;

import hub.com.apiusers.dto.user.UserDTORequest;
import hub.com.apiusers.dto.user.UserDTORequestUpdate;
import hub.com.apiusers.dto.user.UserDTOResponse;
import hub.com.apiusers.projection.user.UserView;
import hub.com.apiusers.util.page.PageResponse;

import java.util.List;

public interface UserService {
    // GET

    // findByIdUser
    UserDTOResponse findByIdUser(Long id);

    // projection
    List<UserView> findActiveUsers();

    // pageListUser
    PageResponse<UserDTOResponse> pageListUser(int page, int size);

    // POST

    // createUser
    UserDTOResponse createUser(UserDTORequest userDTORequest);

    // PUT

    // updateUser
    UserDTOResponse updateUser(Long id, UserDTORequestUpdate userDTORequestUpdate);

    // DELETE

    // deleteUser
    void deleteUser(Long id);


}
