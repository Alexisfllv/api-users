package hub.com.apiusers.mapper;

import hub.com.apiusers.dto.user.UserDTORequest;
import hub.com.apiusers.dto.user.UserDTOResponse;
import hub.com.apiusers.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper (componentModel = "spring",uses = {RoleMapper.class})
public interface UserMapper {

    // entity <- req
    @Mapping(target = "roles", ignore = true)
    User toUser (UserDTORequest userDTORequest);

    // res <- entity
    UserDTOResponse toUserDTOResponse (User user);
}
