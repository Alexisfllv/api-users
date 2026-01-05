package hub.com.apiusers.controller;


import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    // GET

    // findByIdRole
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTOResponse> findByIdRoleGet(@PathVariable Long id){
        RoleDTOResponse dto = roleService.findByIdRole(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
