package hub.com.apiusers.service.mapper;


import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.dto.user.UserDTORequest;
import hub.com.apiusers.dto.user.UserDTOResponse;
import hub.com.apiusers.entity.Role;
import hub.com.apiusers.entity.User;
import hub.com.apiusers.mapper.RoleMapper;
import hub.com.apiusers.mapper.UserMapperImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private UserMapperImpl userMapperImpl;

    @Nested
    @DisplayName("Test mapped entity <- request")
    class toUserDTORequest {

        @Test
        @DisplayName("Should map entity <- request")
        void toUserDTORequestTest(){
            // Arrange
            UserDTORequest req = new UserDTORequest("Alexis","Password","Ferrari","Ferrari@gmail.com",Set.of(1L));

            // Act
            User result = userMapperImpl.toUser(req);

            // Assert
            assertAll(
                    () -> assertEquals("Alexis",result.getUsername()),
                    () -> assertEquals("Password",result.getPassword()),
                    () -> assertEquals("Ferrari", result.getFullName()),
                    () -> assertEquals("Ferrari@gmail.com", result.getEmail())

            );
        }

        @Test
        @DisplayName("Should map user entity <- request null")
        void toUserDTORequestNullTest(){
            User result = userMapperImpl.toUser(null);
            assertNull(result);
        }
    }



    @Nested
    @DisplayName("Test mapped response <- entity")
    class toUserDTOResponseTest{

        @Test
        @DisplayName("Should map response <- entity")
        void toUserDTOResponseTest(){
            // Arrange
            Role role = new Role(1L,"Admin","Detail Admin");
            when(roleMapper.toDTOResponse(role))
                    .thenReturn(new RoleDTOResponse(1L,"Admin","Detail Admin"));

            User user = User.builder()
                    .id(1L)
                    .username("Alexis")
                    .fullName("Ferr")
                    .email("alexis@gmail.com")
                    .roles(Set.of(role))
                    .build();
            // Act
            UserDTOResponse response = userMapperImpl.toUserDTOResponse(user);

            // Assert
            assertAll(
                    () -> assertEquals(1L,response.id()),
                    () -> assertEquals("Alexis",response.username()),
                    () -> assertEquals("Ferr",response.fullName()),
                    () -> assertEquals("alexis@gmail.com",response.email()),
                    () -> assertEquals(1,response.roles().size())
            );

        }

        @Test
        @DisplayName("Should map user response <- entity null")
        void toUserDTOResponseNullTest(){
            UserDTOResponse response = userMapperImpl.toUserDTOResponse(null);
            assertNull(response);
        }
    }
}
