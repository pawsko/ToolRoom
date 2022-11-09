package pl.pawsko.toolroom.status;

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
@Tag(name = "Statuses")
@RequestMapping("/api/status")
public class StatusController {
    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    @Operation(description = "Get all statuses", summary = "Get all statuses")
    @ApiResponse(responseCode = "200", description = "List of all statuses", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = StatusDtoResponse.class)))})
    List<StatusDtoResponse> getAll() {
        return statusService.getAllStatuses();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific status by id", summary = "Get specific status by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Status at provided id was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatusDtoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "The status with the given ID was not found", content = @Content)})
    ResponseEntity<StatusDtoResponse> getById(@PathVariable Long id) {
        return statusService.getStatusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add new status", summary = "Add new status")
    @ApiResponse(responseCode = "201",
            description = "New status has added",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = StatusDtoRequest.class))})
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<StatusDtoResponse> save(@Valid @RequestBody StatusDtoRequest statusDtoRequest) {
        StatusDtoResponse savedStatus = statusService.saveStatus(statusDtoRequest);
        URI savedStatusUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedStatus.getId())
                .toUri();
        return ResponseEntity.created(savedStatusUri).body(savedStatus);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
    @PutMapping("/{id}")
    @Operation(description = "Edit specific status by id", summary = "Edit specific status by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Status successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "The status with the given ID was not found",
                    content = @Content)})
    ResponseEntity<?> replace(@PathVariable Long id, @Valid @RequestBody StatusDtoRequest statusDtoRequest) {
        return statusService.replaceStatus(id, statusDtoRequest)
                .map(c -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
