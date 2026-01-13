package hub.com.apiusers.service.impl;

import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.dto.user.UserDTOResponse;
import hub.com.apiusers.entity.Role;
import hub.com.apiusers.entity.User;
import hub.com.apiusers.mapper.UserMapper;
import hub.com.apiusers.service.domain.RoleServiceDomain;
import hub.com.apiusers.service.domain.UserServiceDomain;
import hub.com.apiusers.util.page.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserServiceDomain userServiceDomain;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    // val
    private Role role;
    private RoleDTOResponse roleDTOResponse;
    private User user;
    private UserDTOResponse userDTOResponse;

    @BeforeEach
    void setUp() {
        role = new Role(1L,"Admin","Dell admin");
        user = new User(1l,"Sara","123","ferr","ale@gmailcom",true, Set.of(role));

        roleDTOResponse = new RoleDTOResponse(1l,"Admin","Dell admin");
        userDTOResponse = new UserDTOResponse(1l,"Sara","ferr","ale@gmailcom",true, Set.of(roleDTOResponse));
    }

    @Nested
    @DisplayName("Get findByIdUser Test")
    class FindByIdUserTest {
        @Test
        @DisplayName("Should finByIdUser Success")
        void testFindByIdUserSuccess() {
            // Arrange
            Long id = 1L;
            when(userServiceDomain.userExists(id)).thenReturn(user);
            when(userMapper.toUserDTOResponse(user)).thenReturn(userDTOResponse);

            // Act
            UserDTOResponse result = userServiceImpl.findByIdUser(id);

            // Assert
            assertAll(
                    () -> assertEquals(userDTOResponse,result),
                    () -> assertEquals(1,result.roles().size())
            );

            // Order - Verify
            InOrder inOrder = Mockito.inOrder(userMapper, userServiceDomain);
            inOrder.verify(userServiceDomain).userExists(id);
            inOrder.verify(userMapper).toUserDTOResponse(user);
        }
    }

    @Nested
    @DisplayName("Get pageListUser Test")
    class FindAllUsersTest{
        @Test
        @DisplayName("Should pageListUser Success")
        void testPageListUserSuccess() {
            // Arrange
            int page = 0;
            int size = 10;
            Pageable pageable = PageRequest.of(page, size);
            Page<User> userPage = new PageImpl<>(
                    List.of(user),
                    pageable,
                    1
            );
            when(userServiceDomain.findAllPage(pageable)).thenReturn(userPage);
            when(userMapper.toUserDTOResponse(user)).thenReturn(userDTOResponse);

            // Act
            PageResponse<UserDTOResponse> result = userServiceImpl.pageListUser(page,size);

            // Assert
            assertAll(
                    () -> assertEquals(0,result.page()),
                    () -> assertEquals(10,result.size()),
                    () -> assertEquals(1,result.totalElements()),
                    () -> assertEquals(userDTOResponse,result.content().get(0))
            );
            // InOrder - Verify
            InOrder inOrder = Mockito.inOrder(userMapper, userServiceDomain);
            inOrder.verify(userServiceDomain).findAllPage(pageable);
            inOrder.verify(userMapper).toUserDTOResponse(user);
        }
    }

}
