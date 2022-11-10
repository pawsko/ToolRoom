package pl.pawsko.toolroom.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "Users")
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userController) {
        this.userService = userController;
    }

    @GetMapping
    @Operation(description = "Get all users")
    @ApiResponse(responseCode = "200", description = "List of all users", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = UserDtoResponse.class)))})
    public List<UserDtoResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific user by id")
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
    @Operation(description = "Add user")
    @ApiResponse(responseCode = "201",
            description = "New user has added",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDtoRequest.class))})
    ResponseEntity<UserDtoResponse> saveUser(@RequestBody UserDtoRequest userDtoRequest) {
        UserDtoResponse savedUser = userService.saveUser(userDtoRequest);
        URI savedUserUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(savedUserUri).body(savedUser);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "The user with the given ID was not found",
                    content = @Content)})
    ResponseEntity<?> replaceUser(@PathVariable Long id, @RequestBody UserDtoRequest userDtoRequest) {
        return userService.replaceUser(id, userDtoRequest)
                .map(userDtoResponse -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
