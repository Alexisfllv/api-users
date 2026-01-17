package hub.com.apiusers.service.domain;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import hub.com.apiusers.entity.Role;
import hub.com.apiusers.repo.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleServiceCvsDomain {

    private final RoleRepo roleRepo;


    // listar los roles existentes
    public List<String> roleExists (){
        return roleRepo.findAll()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());
    }


    // validar e importar
    public List<Role> parseRolesFromCsv(MultipartFile file, List<String> existingNames) throws IOException {

        // Set para control eficiente de duplicados
        Set<String> seenNames = new HashSet<>(existingNames);

        log.warn("Listado de sets"+seenNames);
        log.warn("Listado de existentes"+existingNames);
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withSkipLines(1) // saltar cabecera
                     .build()) {

            // Stream del CSV, map a Role y filtrado de duplicados
            return csvReader
                    .readAll()
                    .stream()
                    .map(columns -> new AbstractMap.SimpleEntry<>(columns[1].trim(), columns[2].trim()))
                    .filter(entry -> seenNames.add(entry.getKey())) // add() retorna false si ya existía
                    .map(entry -> {
                        Role role = new Role();
                        role.setName(entry.getKey());
                        role.setDescription(entry.getValue());
                        return role;
                    })
                    .collect(Collectors.toList());
        } catch (CsvException e) {
            throw new RuntimeException("Error en service Domain CSV");
        }
    }

    // save all
    public void saveAllImport(List<Role> roles) {
        roleRepo.saveAll(roles);
    }
}
