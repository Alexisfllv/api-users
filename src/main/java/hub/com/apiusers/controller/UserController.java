package hub.com.apiusers.controller;

import hub.com.apiusers.dto.user.UserDTOResponse;
import hub.com.apiusers.service.UserService;
import hub.com.apiusers.util.ApiResponse.GenericResponse;
import hub.com.apiusers.util.ApiResponse.StatusApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // GET
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<UserDTOResponse>> findByIdUserGet(@PathVariable Long id){
        UserDTOResponse response = userService.findByIdUser(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponse<>(StatusApi.SUCCESS, response));
    }
}
