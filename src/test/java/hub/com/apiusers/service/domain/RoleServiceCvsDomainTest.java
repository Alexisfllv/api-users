package hub.com.apiusers.service.domain;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import hub.com.apiusers.entity.Role;
import hub.com.apiusers.exception.CsvDomainException;
import hub.com.apiusers.repo.RoleRepo;
import hub.com.apiusers.service.RoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceCvsDomainTest {
    @Mock
    private RoleRepo roleRepo;

    @Spy
    @InjectMocks
    private RoleServiceCvsDomain roleServiceCvsDomain;


    @Nested
    @DisplayName("Test roleExists ")
    class roleExists {
        @Test
        @DisplayName("Test role exists Success ")
        public void roleExistsSucces() {
            // Arrange
            Role r1 = new Role(); r1.setName("name1");
            Role r2 = new Role(); r2.setName("name2");
            Role r3 = new Role(); r3.setName("name3");

            when(roleRepo.findAll()).thenReturn(List.of(r1, r2, r3));

            // Act
            List<String> response = roleServiceCvsDomain.roleExists();

            // Assert
            assertEquals(3, response.size());
            assertEquals(List.of("name1", "name2", "name3"), response);

        }
    }

    @Nested
    @DisplayName("Test parseRolesFromCsv")
    class parseRolesFromCsv {
        @Test
        @DisplayName("Test parseRolesFromCsv Success")
        public void parseRolesFromCsvSucces() throws Exception {
            // Arrange
            String csv = """
                id,name,description
                1,ADMIN,Administrador
                2,USER,Usuario
                3,ADMIN,Duplicado
                """;

            MultipartFile file = new MockMultipartFile(
                    "file",
                    "roles.csv",
                    "text/csv",
                    csv.getBytes(StandardCharsets.UTF_8)
            );

            List<String> existingNames = List.of("GUEST");

            // Act

            List<Role> result = roleServiceCvsDomain.parseRolesFromCsv(file,existingNames);


            // Assert
            assertAll(
                    () -> assertEquals(2,result.size()),
                    () -> assertTrue(result.stream().anyMatch(r -> r.getName().equals("ADMIN"))),
                    () -> assertTrue(result.stream().anyMatch(r -> r.getName().equals("USER")))
            );
        }

        @Test
        @DisplayName("Test parseRolesFromCsv_shouldIgnoreDuplicateNames")
        public void parseRolesFromCsv_shouldIgnoreDuplicateNamesFail() throws IOException, CsvException {
            // Arrange
            String csv = """
                id,name,description
                1,ADMIN,Administrador
                2,ADMIN,Duplica
                3,USER,Usuario
                """;

            MultipartFile file = new MockMultipartFile(
                    "file",
                    "roles.csv",
                    "text/csv",
                    csv.getBytes(StandardCharsets.UTF_8)
            );

            // Act
            List<Role> roles = roleServiceCvsDomain.parseRolesFromCsv(file, List.of());

            // Assert
            assertEquals(2, roles.size());
            assertTrue(roles.stream().anyMatch(r -> r.getName().equals("ADMIN")));
            assertTrue(roles.stream().anyMatch(r -> r.getName().equals("USER")));

        }
        @Test
        @DisplayName("Test parseRolesFromCsv Throw  CsvDomainException ")
        void parseRolesFromCsv_shouldThrowCsvDomainException_whenCsvReaderFails() throws Exception {
            MultipartFile file = new MockMultipartFile(
                    "file",
                    "roles.csv",
                    "text/csv",
                    "id,name,desc".getBytes()
            );

            // Mock CSVReader para forzar la excepción
            CSVReader csvReader = mock(CSVReader.class);
            doThrow(new CsvException("fail"))
                    .when(csvReader).readAll();

            // Mock del metodo createCsvReader para usar nuestro CSVReader falso
            doReturn(csvReader)
                    .when(roleServiceCvsDomain).createCsvReader(any());

            CsvDomainException ex = assertThrows(
                    CsvDomainException.class,
                    () -> roleServiceCvsDomain.parseRolesFromCsv(file, List.of())
            );

            assertEquals("Error al procesar CSV", ex.getMessage());
            assertNotNull(ex.getCause());
        }
    }

    @DisplayName("Save All import")
    @Nested
    class saveAllImport {
        @Test
        @DisplayName("Save All import Success")
        public void saveAllImport() {
            // Act
            List<Role> roles = Arrays.asList(new Role(1L,"Sabs","Subscip"));
            // Arrange
            roleServiceCvsDomain.saveAllImport(roles);
            // Assert
            verify(roleRepo, times(1)).saveAll(roles);
        }
    }



}

