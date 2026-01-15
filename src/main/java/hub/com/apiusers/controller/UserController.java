package hub.com.apiusers.controller;

import hub.com.apiusers.dto.user.UserDTORequest;
import hub.com.apiusers.dto.user.UserDTORequestUpdate;
import hub.com.apiusers.dto.user.UserDTOResponse;
import hub.com.apiusers.service.UserService;
import hub.com.apiusers.util.ApiResponse.GenericResponse;
import hub.com.apiusers.util.ApiResponse.StatusApi;
import hub.com.apiusers.util.page.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
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

    @GetMapping("/page")
    public ResponseEntity<GenericResponse<PageResponse<UserDTOResponse>>> pageListUserGet(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "3") int size){
        PageResponse<UserDTOResponse> paged = userService.pageListUser(page, size);
        return  ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponse<>(StatusApi.SUCCESS, paged));
    }


    // POST

    @PostMapping
    public ResponseEntity<GenericResponse<UserDTOResponse>> createUserPost( @Valid @RequestBody UserDTORequest request){
        UserDTOResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GenericResponse<>(StatusApi.CREATED, response));
    }

    // PUT

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<UserDTOResponse>> updateUserPut(@PathVariable Long id, @Valid @RequestBody UserDTORequestUpdate request){
        UserDTOResponse response = userService.updateUser(id, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponse<>(StatusApi.SUCCESS, response));
    }

}
