package pl.pawsko.toolroom.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.pawsko.toolroom.hellpers.UriHelper;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users")
@RequestMapping("/api/user")
class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(description = "Get all users")
    @ApiResponse(responseCode = "200", description = "List of all users", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = UserDtoResponse.class)))})
    List<UserDtoResponse> getAllUsers() {
        log.debug("Getting all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User at provided id was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDtoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "User with the given ID was not found", content = @Content)})
    ResponseEntity<UserDtoResponse> getUserById(@PathVariable Long id) {
        log.debug("Getting user by id={}", id);
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add user")
    @ApiResponse(responseCode = "201",
            description = "New user has added",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDtoRequest.class))})
    ResponseEntity<UserDtoResponse> saveUser(@Valid @RequestBody UserDtoRequest userDtoRequest) {
        UserDtoResponse savedUser = userService.saveUser(userDtoRequest);
        URI savedUserUri = UriHelper.getUri(savedUser.getId());
        log.debug("Saved new user {}", savedUser);
        return ResponseEntity.created(savedUserUri).body(savedUser);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User with the given ID was not found",
                    content = @Content)})
    ResponseEntity<?> replaceUser(@PathVariable Long id, @Valid@RequestBody UserDtoRequest userDtoRequest) {
        log.debug("Replaced user id={}", id);
        return userService.replaceUser(id, userDtoRequest)
                .map(userDtoResponse -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Bad request 400");
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}
