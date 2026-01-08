package hub.com.apiusers.service.domain;

import hub.com.apiusers.entity.Role;
import hub.com.apiusers.exception.ResourceNotFoundException;
import hub.com.apiusers.repo.RoleRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceDomainTest {

    @Mock
    private RoleRepo roleRepo;

    @InjectMocks
    private RoleServiceDomain roleServiceDomain;

    // default
    private Role role;

    @BeforeEach
    public void setUp() {
        role = new Role(1L,"Admin","Admin Detail");
    }


    @Nested
    @DisplayName("Test find id Roles")
    class findIdRolesTest{

        @Test
        @DisplayName("Test find id Roles Success")
        void testFindIdRolesSuccess(){
            // Arrange
            Long roleIdExist = 1L;
            Role roleExist = role;
            when(roleRepo.findById(roleIdExist)).thenReturn(Optional.of(roleExist));

            // Act
            Role result = roleServiceDomain.roleExists(roleIdExist);

            // Assert
            assertAll(
                    () -> assertEquals(1L,result.getId()),
                    () -> assertEquals("Admin",result.getName()),
                    () -> assertEquals("Admin Detail",result.getDescription())
            );

            // Verify
            verify(roleRepo).findById(roleIdExist);


        }

        @Test
        @DisplayName("Test find id Roles Not Found")
        void testFindIdRolesNotFound() {
            Long roleId = 99L;
            when(roleRepo.findById(roleId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> roleServiceDomain.roleExists(roleId));

            verify(roleRepo).findById(roleId);
            verifyNoMoreInteractions(roleRepo);
        }

    }
}
