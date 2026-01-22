package hub.com.apiusers.service.mapper;

import hub.com.apiusers.dto.role.RoleDTORequest;
import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.entity.Role;
import hub.com.apiusers.mapper.RoleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class RoleMapperTest {

    private RoleMapper roleMapper;

    @BeforeEach
    void setUp() {
        roleMapper = Mappers.getMapper(RoleMapper.class);
    }

    @Nested
    @DisplayName("Tests mapped entity <- request")
    class ToRoleTests {

        @Test
        @DisplayName("Should map entity <- request")
        void toRole_shouldMappedRequestCorrectly() {
            // Arrange
            RoleDTORequest req = new RoleDTORequest("Admin","Detail Admin");

            // Act
            Role result = roleMapper.toRole(req);

            // Assert
            assertAll(
                    () -> assertEquals("Admin",result.getName()),
                    () -> assertEquals("Detail Admin",result.getDescription())
            );
        }

        @Test
        @DisplayName("Should map entity <- request null")
        void toRole_shouldMapNullRequest() {
            // Act
            Role result = roleMapper.toRole(null);

            // Assert
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("Tests mapped response <- entity")
    class toDTOResponseTests {

        @Test
        @DisplayName("Should map response <- entity")
        void toDTOResponse_shouldMappedResponseCorrectly() {
            // Arrange
            Role entity = new Role(1L,"Admin","Detail Admin");
            // Act
            RoleDTOResponse result = roleMapper.toDTOResponse(entity);
            // Assert
            assertAll(
                    () -> assertEquals(1L,result.id()),
                    () -> assertEquals("Admin", result.name()),
                    () -> assertEquals("Detail Admin", result.description())
            );
        }

        @Test
        @DisplayName("Shoukd map response <- entity null")
        void toDTOResponse_shouldMapNullResponse() {
            // Act
            RoleDTOResponse result = roleMapper.toDTOResponse(null);
            // Assert
            assertNull(result);
        }
    }
}
