package hub.com.apiusers.service.domain;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import hub.com.apiusers.entity.Role;
import hub.com.apiusers.exception.CsvDomainException;
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

    CSVReader createCsvReader(Reader reader) {
        return new CSVReaderBuilder(reader)
                .withSkipLines(1) // saltar cabecera
                .build();
    }

    // validar e importar
    public List<Role> parseRolesFromCsv(MultipartFile file, List<String> existingNames) throws IOException {
        // Set para control eficiente de duplicados
        Set<String> seenNames = new HashSet<>(existingNames);

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVReader csvReader = createCsvReader(reader)) {

            // Leemos todas las líneas, mapeamos a Role y filtramos duplicados
            return csvReader.readAll()
                    .stream()
                    .map(c -> new AbstractMap.SimpleEntry<>(c[1].trim(), c[2].trim()))
                    .filter(e -> seenNames.add(e.getKey())) // add() retorna false si ya existía
                    .map(e -> {
                        Role r = new Role();
                        r.setName(e.getKey());
                        r.setDescription(e.getValue());
                        return r;
                    })
                    .toList();

        } catch (CsvException e) {
            // Se lanza excepción de dominio profesional
            throw new CsvDomainException("Error al procesar CSV", e);
        }
    }

    // save all
    public void saveAllImport(List<Role> roles) {
        roleRepo.saveAll(roles);
    }
}
