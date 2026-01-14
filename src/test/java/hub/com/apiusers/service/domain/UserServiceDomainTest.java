package hub.com.apiusers.service.domain;

import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.dto.user.UserDTOResponse;
import hub.com.apiusers.entity.Role;
import hub.com.apiusers.entity.User;
import hub.com.apiusers.exception.ResourceNotFoundException;
import hub.com.apiusers.exception.UniqueException;
import hub.com.apiusers.nums.ExceptionMessages;
import hub.com.apiusers.repo.RoleRepo;
import hub.com.apiusers.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceDomainTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private RoleRepo roleRepo;

    @InjectMocks
    private UserServiceDomain userServiceDomain;

    // default
    private Role role;
    private User user;


    @BeforeEach
    public void setUp() {
        role = new Role(1L,"Admin","Admin Detail");
        user = new User(1L, "sara", "password", "ferr", "al@gmailcom", true, Set.of(role));
    }

    @Nested
    @DisplayName("Test find id User")
    class findIdUserTest {

        @Test
        @DisplayName("Test find id User Success")
        public void testFindIdUserSuccess() {
            // Arrange
            Long userIdExist = 1L;
            User userExist = user;
            when(userRepo.findById(userIdExist)).thenReturn(Optional.of(userExist));

            // Act
            User result = userServiceDomain.userExists(userIdExist);

            // Assert
            assertAll(
                    () -> assertEquals(user,result),
                    () -> assertEquals(1,result.getRoles().size())
            );

            // Verify
            verify(userRepo).findById(userIdExist);
        }

        @Test
        @DisplayName("Test find id Ussert Throw")
        public void testFindIdUserThrow() {
            // Arrange
            Long idUserNotExist = 1L;
            when(userRepo.findById(idUserNotExist)).thenReturn(Optional.empty());
            // Act
            // Assert
            assertThrows(ResourceNotFoundException.class,
                    () -> userServiceDomain.userExists(idUserNotExist));

            // Verify
            verify(userRepo).findById(idUserNotExist);
            verifyNoMoreInteractions(userRepo);
        }

    }

    @Nested
    @DisplayName("Test findAll Page")
    class findAllPageTest{
        @Test
        @DisplayName("Test find All User Success")
        public void testFindAllUserSuccess(){
            // Arrange
            int page = 1;
            int size = 3;
            Pageable pageable = PageRequest.of(page, size);
            Page<User> expected = Page.empty(pageable);
            when(userRepo.findAll(pageable)).thenReturn(expected);
            // Act
            Page<User> result = userServiceDomain.findAllPage(pageable);
            // Assert
            assertSame(expected,result);
            // Verify
            verify(userRepo).findAll(pageable);
        }
    }

    @Nested
    @DisplayName("Test validateUniqueUsername")
    class ValidateUniqueUsernameTest{
        @Test
        @DisplayName("Test validateUniqueUsername Success")
        public void testValidateUniqueUsernameSuccess(){
            // Arrange
            String username = "sara";
            when(userRepo.existsByUsername(username)).thenReturn(false);
            // Act and Assert
            assertDoesNotThrow(
                    () -> userServiceDomain.validateUniqueUsername(username));
            // Verify
            verify(userRepo).existsByUsername(username);
        }

        @Test
        @DisplayName("test validateUniqueUsername Fail")
        public void testValidateUniqueUsernameFail(){
            // Arrange
            String usernameExist = "sara";
            when(userRepo.existsByUsername(usernameExist)).thenReturn(true);

            // Act and Assert
            UniqueException exUn = assertThrows(UniqueException.class,
                    () -> userServiceDomain.validateUniqueUsername(usernameExist));

            assertEquals(ExceptionMessages.UNIQUE_EXC.message()+usernameExist,exUn.getMessage());
            // Verify
            verify(userRepo).existsByUsername(usernameExist);
        }
    }

    @Nested
    @DisplayName("Test validateUniqueEmail")
    class ValidateUniqueEmailTest{

        @Test
        @DisplayName("Test validateUniqueEmail Success")
        public void testValidateUniqueEmailSuccess(){
            // Arrange
            String email = "sara@gmail.com";
            when(userRepo.existsByEmail(email)).thenReturn(false);
            // Act and Assert
            assertDoesNotThrow(
                    () -> userServiceDomain.validateUniqueEmail(email));
            // Verify
            verify(userRepo).existsByEmail(email);
        }

        @Test
        @DisplayName("test validateUniqueEmailFail")
        public void testValidateUniqueEmailFail(){
            // Arrange
            String emailExist = "sara@gmail.com";
            when(userRepo.existsByEmail(emailExist)).thenReturn(true);
            // Act and Assert
            UniqueException exUn = assertThrows(UniqueException.class,
                    () -> userServiceDomain.validateUniqueEmail(emailExist));
            assertEquals(ExceptionMessages.UNIQUE_EXC.message()+emailExist,exUn.getMessage());
            // Verify
            verify(userRepo).existsByEmail(emailExist);
        }
    }

    @Nested
    @DisplayName("Test validateRoleExists")
    class findUserByIdTest{
        @Test
        @DisplayName("Test validateRoleExists Success")
        public void testValidateRoleExistsSuccess(){
            // Arrange
            Set<Long> rolesId = Set.of(1L, 2L);
            Role r1 = new Role(1L, "Admin","Det Admin");
            Role r2 = new Role(2L, "User","Det User");
            when(roleRepo.findAllById(rolesId)).thenReturn(List.of(r1,r2));
            // Act
            Set<Role> result = userServiceDomain.validateRoleExists(rolesId);

            // Assert
            assertAll(
                    () -> assertEquals(2,result.size()),
                    () -> assertTrue(result.contains(r1)),
                    () -> assertTrue(result.contains(r2))
            );
            // Verify
            verify(roleRepo).findAllById(rolesId);
        }

        @Test
        @DisplayName("Test validateRoleExists Fail")
        public void testValidateRoleExistsFail(){
            // Arrange
            Long roleIdNotExist = 2L;
            Set<Long> rolesId = Set.of(1L, roleIdNotExist);
            Role r1 = new Role(1L, "Admin","Det Admin");
            when(roleRepo.findAllById(rolesId)).thenReturn(List.of(r1));
            // Act
            // Assert
            ResourceNotFoundException exRes= assertThrows(ResourceNotFoundException.class,
                    () -> userServiceDomain.validateRoleExists(rolesId)
            );
            assertEquals(ExceptionMessages.RESOURCE_NOT_FOUND_ERROR.message()+rolesId,exRes.getMessage());

            // Verify
            verify(roleRepo).findAllById(rolesId);
        }

    }

    @Nested
    @DisplayName("Test saveUser")
    class saveUserTest{
        @Test
        @DisplayName("Test saveUser Success")
        public void testSaveUserSuccess(){
            // Arrange
            User userSaved = user;
            userSaved.setId(1L);
            when(userRepo.save(user)).thenReturn(userSaved);
            // Act
            User result = userServiceDomain.saveUser(user);
            // Assert
            assertAll(
                    () -> assertEquals(userSaved,result)
            );
            verify(userRepo).save(user);
            verifyNoMoreInteractions(userRepo);
        }
    }
}
