package pl.pawsko.toolroom.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Users")
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userController) {
        this.userService = userController;
    }

    @GetMapping
    @Operation(description = "Get all users", summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "List of all users", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = UserDtoResponse.class)))})
    public List<UserDtoResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific user by id", summary = "Get specific user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User at provided id was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDtoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "The user with the given ID was not found", content = @Content)})
    public ResponseEntity<UserDtoResponse> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add user", summary = "Add user")
    @ApiResponse(responseCode = "201",
            description = "New user has added",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDtoRequest.class))})
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<UserDtoResponse> saveUser(@Valid @RequestBody UserDtoRequest userDtoRequest) {
        UserDtoResponse savedUser = userService.saveUser(userDtoRequest);
        URI savedUserUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(savedUserUri).body(savedUser);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific user by id", summary = "Edit specific user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "The user with the given ID was not found",
                    content = @Content)})
    ResponseEntity<?> replaceUser(@PathVariable Long id, @Valid @RequestBody UserDtoRequest userDtoRequest) {
        return userService.replaceUser(id, userDtoRequest)
                .map(c -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
