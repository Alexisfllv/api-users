package hub.com.apiusers.controller;


import hub.com.apiusers.dto.role.RoleDTOResponse;
import hub.com.apiusers.service.RoleService;
import hub.com.apiusers.util.ApiResponse.GenericResponse;
import hub.com.apiusers.util.ApiResponse.StatusApi;
import hub.com.apiusers.util.page.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    // GET

    // findByIdRole
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<RoleDTOResponse>> findByIdRoleGet(@PathVariable Long id){
        RoleDTOResponse dto = roleService.findByIdRole(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponse<>(StatusApi.SUCCESS,dto));
    }

    // pageListRole
    @GetMapping("/page")
    public ResponseEntity<GenericResponse<PageResponse<RoleDTOResponse>>> pageListRoleGet(
            @RequestParam (defaultValue = "0")int page,
            @RequestParam (defaultValue = "3")int size) {
        PageResponse<RoleDTOResponse> paged = roleService.pageListRole(page, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponse<>(StatusApi.SUCCESS,paged));
    }
}
