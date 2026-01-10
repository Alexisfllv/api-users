package hub.com.apiusers.service.domain;

import hub.com.apiusers.dto.role.RoleDTORequest;
import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.entity.Role;
import hub.com.apiusers.exception.ResourceNotFoundException;
import hub.com.apiusers.exception.UniqueException;
import hub.com.apiusers.nums.ExceptionMessages;
import hub.com.apiusers.repo.RoleRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
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

    @Nested
    @DisplayName("Test page List Roles")
    class pageListRolesTest{

        @Test
        @DisplayName("Test page list Roles")
        void testPageListRolesSuccess(){
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            Page<Role> expected = Page.empty(pageable);
            when(roleRepo.findAll(pageable)).thenReturn(expected);
            // Act
            Page<Role> result = roleServiceDomain.findAll(pageable);

            // Assert
            assertSame(expected, result);

            verify(roleRepo).findAll(pageable);
        }
    }

    @Nested
    @DisplayName("Test save Role")
    class saveRoleTest{

        @Test
        @DisplayName("Test save Role Success")
        void testSaveRoleSuccess(){
            // Arrange
            when(roleRepo.save(role)).thenReturn(role);
            // Act
            roleServiceDomain.saveRole(role);
            // Assert
            verify(roleRepo).save(role);

        }
    }

    @Nested
    @DisplayName("Test validate Name Role")
    class roleNameUniqueTest{
        @Test
        @DisplayName("Test validate Name Role Success")
        void testValidateNameRoleSuccess(){
            // Arrange
            String name = "Admin";
            when(roleRepo.existsByName(name)).thenReturn(Boolean.FALSE);
            // Act & Assert
            assertDoesNotThrow(() -> roleServiceDomain.roleNameUnique(name));
        }

        @Test
        @DisplayName("Test validate Name Role Exception")
        void testValidateNameRoleException() {
            // Arrange
            String nameExist = "Admin";
            when(roleRepo.existsByName(nameExist)).thenReturn(Boolean.TRUE);

            // Act
            UniqueException ex = assertThrows(UniqueException.class,
                    () -> roleServiceDomain.roleNameUnique(nameExist));

            // Assert
            assertTrue(ex.getMessage().contains(ExceptionMessages.UNIQUE_EXC.message()));
        }
    }
}
