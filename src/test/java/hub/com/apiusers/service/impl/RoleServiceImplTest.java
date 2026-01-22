package hub.com.apiusers.service.impl;

import hub.com.apiusers.dto.role.RoleDTORequest;
import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.entity.Role;
import hub.com.apiusers.mapper.RoleMapper;
import hub.com.apiusers.service.domain.RoleServiceCvsDomain;
import hub.com.apiusers.service.domain.RoleServiceDomain;
import hub.com.apiusers.util.page.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private RoleServiceDomain roleServiceDomain;

    @Mock
    private RoleServiceCvsDomain roleServiceCvsDomain;

    @InjectMocks
    private RoleServiceImpl roleServiceImpl;

    // val
    private Role roleEntity;
    private RoleDTORequest roleRequest;
    private Role roleEmpty;
    private RoleDTOResponse roleResponse;

    @BeforeEach
    void setUp() {
        roleEmpty = new Role(null,"Admin","Detail Admin");
        roleRequest = new RoleDTORequest("Admin","Detail Admin");
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

    @Nested
    @DisplayName("Get pageListRoles Test")
    class GetPageListRolesTest {

        @Test
        @DisplayName("Should pageListRoles Success")
        void testPageListRolesSuccess() {
            // Arrange
            int page = 0;
            int size = 10;
            Pageable pageable = PageRequest.of(page, size);
            Page<Role> rolePage = new PageImpl<>(
                    List.of(roleEntity),
                    pageable,
                    1
            );
            when(roleServiceDomain.findAll(pageable)).thenReturn(rolePage);
            when(roleMapper.toDTOResponse(roleEntity)).thenReturn(roleResponse);

            // Act
            PageResponse<RoleDTOResponse> result = roleServiceImpl.pageListRole(page, size);

            // Assert
            assertAll(
                    () -> assertEquals(0,result.page()),
                    () -> assertEquals(10,result.size()),
                    () -> assertEquals(1,result.totalElements()),
                    () -> assertEquals(roleResponse,result.content().get(0))
            );
            // InOrder Verify
            InOrder inOrder = Mockito.inOrder(roleServiceDomain,roleMapper);
            inOrder.verify(roleServiceDomain).findAll(pageable);
            inOrder.verify(roleMapper).toDTOResponse(roleEntity);


        }
    }

    @Nested
    @DisplayName("Post createRole Test")
    class PostCreateRoleTest {

        @Test
        @DisplayName("Should createRole Success")
        void testCreateRoleSuccess() {
            // Arrange
            when(roleMapper.toRole(roleRequest)).thenReturn(roleEmpty);
            when(roleServiceDomain.saveRole(roleEmpty)).thenReturn(roleEntity);
            when(roleMapper.toDTOResponse(roleEntity)).thenReturn(roleResponse);


            // Act
            RoleDTOResponse result = roleServiceImpl.createRole(roleRequest);

            // Assert
            assertAll(
                    () -> assertEquals(1L,result.id()),
                    () -> assertEquals(roleRequest.name(),result.name()),
                    () -> assertEquals(roleRequest.description(),result.description())
            );

            InOrder inOrder = Mockito.inOrder(roleMapper,roleServiceDomain);
            inOrder.verify(roleMapper).toRole(roleRequest);
            inOrder.verify(roleServiceDomain).roleNameUnique(roleRequest.name());
            inOrder.verify(roleServiceDomain).saveRole(roleEmpty);
            inOrder.verify(roleMapper).toDTOResponse(roleEntity);

        }
    }

    @Nested
    @DisplayName("Post importRolesFromCsv")
    class PostImportRolesFromCsvTest {
        @Test
        @DisplayName("Should importRolesFromCsv Success")
        void testImportRolesFromCsvSuccess() throws Exception {
            // Arrange
            MultipartFile file = new MockMultipartFile(
                    "file",
                    "roles.csv",
                    "text/csv",
                    "id,name,desc\n1,ADMIN,Administrador".getBytes()
            );

            List<String> existingNames = List.of("GUEST");
            Role roleC = new Role();
            roleC.setId(1L);
            roleC.setName("ADMIN");
            roleC.setDescription("Administrador");

            RoleDTOResponse dtoC = new RoleDTOResponse(1L,"ADMIN", "Administrador");


            // Mocks
            when(roleServiceCvsDomain.roleExists()).thenReturn(existingNames);
            when(roleServiceCvsDomain.parseRolesFromCsv(file, existingNames))
                    .thenReturn(List.of(roleC));
            doNothing().when(roleServiceCvsDomain).saveAllImport(List.of(roleC));
            when(roleMapper.toDTOResponse(roleC)).thenReturn(dtoC);

            // Act
            List<RoleDTOResponse> result = roleServiceImpl.importRolesFromCsv(file);

            // Assert
            assertEquals(1, result.size());
            assertEquals("ADMIN", result.get(0).name());
            assertEquals("Administrador", result.get(0).description());

            // Verify flujo
            verify(roleServiceCvsDomain).roleExists();
            verify(roleServiceCvsDomain).parseRolesFromCsv(file, existingNames);
            verify(roleServiceCvsDomain).saveAllImport(List.of(roleC));
            verify(roleMapper).toDTOResponse(roleC);
        }
    }

    @Nested
    @DisplayName("Put updateRole Test")
    class PutUpdateRoleTest {

        @Test
        @DisplayName("Should updateRole Success")
        void testUpdateRoleSuccess() {
            // Arrange
            Long roleIdExist = 1L;
            RoleDTORequest roleUpdateRequest = new RoleDTORequest("Administrator","Detail Administrator");
            Role roleUpdate = new Role(1L,"Administrator","Detail Administrator");
            RoleDTOResponse roleUpdateResponse = new RoleDTOResponse(1L,"Administrator","Detail Administrator");
            when(roleServiceDomain.roleExists(roleIdExist)).thenReturn(roleEntity);
            when(roleServiceDomain.saveRole(roleEntity)).thenReturn(roleUpdate);
            when(roleMapper.toDTOResponse(roleUpdate)).thenReturn(roleUpdateResponse);
            // Act
            RoleDTOResponse result = roleServiceImpl.updateRole(roleUpdateRequest,roleIdExist);

            // Assert
            assertAll(
                    () -> assertEquals(roleIdExist,result.id()),
                    () -> assertEquals("Administrator",result.name()),
                    () -> assertEquals("Detail Administrator",result.description())
            );

            // Verify
            InOrder inOrder = Mockito.inOrder(roleMapper,roleServiceDomain);
            inOrder.verify(roleServiceDomain).roleExists(roleIdExist);
            inOrder.verify(roleServiceDomain).roleNameUnique(roleUpdateRequest.name());
            inOrder.verify(roleServiceDomain).saveRole(roleEntity);
            inOrder.verify(roleMapper).toDTOResponse(roleUpdate);

        }
    }

    @Nested
    @DisplayName("Delete role Test")
    class DeleteRoleTest {
        @Test
        @DisplayName("Should deleteRole Success")
        void testDeleteRoleSuccess() {
            // Arrange
            Long idRoleExist = 1L;
            when(roleServiceDomain.roleExists(idRoleExist)).thenReturn(roleEntity);
            // Act

            roleServiceImpl.deleteRole(idRoleExist);
            // Assert & Verify
            verify(roleServiceDomain).roleExists(idRoleExist);
            verify(roleServiceDomain).deleteIdRole(idRoleExist);
        }
    }
}
