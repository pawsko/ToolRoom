package pl.pawsko.toolroom.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.pawsko.toolroom.category.CategoryDto;

import java.net.URI;

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
    public Iterable<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific user byID", summary = "Get specific user byID")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add user", summary = "Add user")
    ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) {
        UserDto savedUser = userService.saveUser(userDto);
        URI savedUserUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(savedUserUri).body(savedUser);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific user byID", summary = "Edit specific user byID")
    ResponseEntity<?> replaceUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return userService.replaceUser(id, userDto)
                .map(c -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
