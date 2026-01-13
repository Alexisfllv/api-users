package hub.com.apiusers.service.domain;

import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.dto.user.UserDTOResponse;
import hub.com.apiusers.entity.Role;
import hub.com.apiusers.entity.User;
import hub.com.apiusers.exception.ResourceNotFoundException;
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

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceDomainTest {

    @Mock
    private UserRepo userRepo;

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
    class FindIdUserTest {

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
    class FindAllPageTest{
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
}
