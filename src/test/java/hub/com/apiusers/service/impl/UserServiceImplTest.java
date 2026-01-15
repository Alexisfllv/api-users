package hub.com.apiusers.service.impl;

import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.dto.user.UserDTORequest;
import hub.com.apiusers.dto.user.UserDTORequestUpdate;
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
import static org.mockito.Mockito.doNothing;
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

    @Nested
    @DisplayName("Post createUser Test")
    class SaveUserTest{
        @Test
        @DisplayName("Should createUser success")
        void testCreateUserSuccess() {
            // Arrange
            UserDTORequest reqEmpty = new UserDTORequest("Sara","123","ferr","ale@gmailcom", Set.of(role.getId()));
            User userEmpty = new User(null,"Sara","123","ferr","ale@gmailcom",null, Set.of(role));
            when(userMapper.toUser(reqEmpty)).thenReturn(userEmpty);
            Set<Role> rolesExist = Set.of(role);
            when(userServiceDomain.validateRoleExists(reqEmpty.roles())).thenReturn(rolesExist);
            User userSaved = new User(1L,"Sara","123","ferr","ale@gmailcom",null, Set.of(role));
            when(userServiceDomain.saveUser(userEmpty)).thenReturn(userSaved);
            when(userMapper.toUserDTOResponse(userSaved)).thenReturn(userDTOResponse);

            // Act
            UserDTOResponse response = userServiceImpl.createUser(reqEmpty);

            // Assert
            assertAll(
                    () -> assertEquals(userDTOResponse,response)
            );

            // InOrder - Verify
            InOrder inOrder = Mockito.inOrder(userMapper, userServiceDomain);
            inOrder.verify(userMapper).toUser(reqEmpty);
            inOrder.verify(userServiceDomain).validateUniqueUsername(reqEmpty.username());
            inOrder.verify(userServiceDomain).validateUniqueEmail(reqEmpty.email());
            inOrder.verify(userServiceDomain).validateRoleExists(reqEmpty.roles());
            inOrder.verify(userServiceDomain).saveUser(userEmpty);
            inOrder.verify(userMapper).toUserDTOResponse(userSaved);
        }
    }

    @Nested
    @DisplayName("Put updateUser Test")
    class UpdateUserTest{
        @Test
        @DisplayName("Should updateUser success")
        void testUpdateUserSuccess() {
            // Arrange
            User userExist = new  User(1L,"Sara","123","ferr","ale@gmailcom",true, Set.of(role));
            UserDTORequestUpdate userDTOupdate = new UserDTORequestUpdate("Sera","321","Sera less","sera@gmailcom",true, Set.of(role.getId()));
            Long idUserExist = userExist.getId();
            Set<Role> rolesExist = Set.of(role);
            User userUpdate = new User(1L,"Sera","321","Sera less","sera@gmailcom",true, Set.of(role));
            UserDTOResponse userUpdateResponse = new UserDTOResponse(1L,"Sera","Sera less","sera@gmailcom",true, Set.of(roleDTOResponse));

            when(userServiceDomain.userExists(idUserExist)).thenReturn(userExist);
            doNothing().when(userServiceDomain).validateUniqueUsername(userDTOupdate.username());
            doNothing().when(userServiceDomain).validateUniqueEmail(userDTOupdate.email());
            when(userServiceDomain.validateRoleExists(userDTOupdate.roles())).thenReturn(rolesExist);
            when(userServiceDomain.saveUser(userExist)).thenReturn(userUpdate);
            when(userMapper.toUserDTOResponse(userUpdate)).thenReturn(userUpdateResponse);
            // Act
            UserDTOResponse response = userServiceImpl.updateUser(idUserExist,userDTOupdate);
            // Assert
            assertAll(
                    () -> assertEquals(userUpdateResponse,response)
            );

            // In Order - Verify
            InOrder inOrder = Mockito.inOrder(userMapper, userServiceDomain);
            inOrder.verify(userServiceDomain).userExists(idUserExist);
            inOrder.verify(userServiceDomain).validateUniqueUsername(userDTOupdate.username());
            inOrder.verify(userServiceDomain).validateUniqueEmail(userDTOupdate.email());
            inOrder.verify(userServiceDomain).validateRoleExists(userDTOupdate.roles());
            inOrder.verify(userServiceDomain).saveUser(userExist);
            inOrder.verify(userMapper).toUserDTOResponse(userUpdate);
        }
    }

    @Nested
    @DisplayName("Delete deleteUser Test")
    class DeleteUserTest{
        @Test
        @DisplayName("Should deleteUser success")
        void testDeleteUserSuccess() {
            // Arrange
            Long idUserExist = 1L;
            User userDelete = user;
            when(userServiceDomain.userExists(idUserExist)).thenReturn(userDelete);
            // Act
            userServiceImpl.deleteUser(idUserExist);
            // Assert
            InOrder inOrder = Mockito.inOrder(userServiceDomain);
            inOrder.verify(userServiceDomain).userExists(idUserExist);
            inOrder.verify(userServiceDomain).deleteUser(userDelete);
            inOrder.verifyNoMoreInteractions();
        }
    }

}
