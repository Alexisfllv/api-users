package hub.com.apiusers.service.impl;

import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.entity.Role;
import hub.com.apiusers.mapper.RoleMapper;
import hub.com.apiusers.service.domain.RoleServiceDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private RoleServiceDomain roleServiceDomain;

    @InjectMocks
    private RoleServiceImpl roleServiceImpl;

    // val
    private Role roleEntity;
    private RoleDTOResponse roleResponse;

    @BeforeEach
    void setUp() {
        roleEntity = new Role(1L,"Admin","Detail Admin");
        roleResponse = new RoleDTOResponse(1L,"Admin","Detail Admin");
    }

    @Nested
    @DisplayName("Get findByIdRole Test")
    class FindByIdRoleTest {

        @Test
        @DisplayName("Should findByIdRole Success")
        void testFindByIdRoleSuccess() {
            // Arrange
            Long roleIdExist = 1L;

            when(roleServiceDomain.roleExists(roleIdExist)).thenReturn(roleEntity);
            when(roleMapper.toDTOResponse(roleEntity)).thenReturn(roleResponse);
            // Act
            RoleDTOResponse result = roleServiceImpl.findByIdRole(roleIdExist);

            // Assert
            assertAll(
                    () -> assertEquals(roleIdExist,result.id()),
                    () -> assertEquals("Admin",result.name()),
                    () -> assertEquals("Detail Admin",result.description())
            );

            // InOrder Verify
            InOrder inOrder = Mockito.inOrder(roleMapper,roleServiceDomain);
            inOrder.verify(roleServiceDomain).roleExists(roleIdExist);
            inOrder.verify(roleMapper).toDTOResponse(roleEntity);

        }
    }
}
