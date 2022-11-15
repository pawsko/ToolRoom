package pl.pawsko.toolroom.status;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "Statuses")
@RequestMapping("/api/status")
class StatusController {
    private final StatusService statusService;

    @GetMapping
    @Operation(description = "Get all statuses")
    @ApiResponse(responseCode = "200", description = "List of all statuses", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = StatusDtoResponse.class)))})
    List<StatusDtoResponse> getAllStatuses() {
        return statusService.getAllStatuses();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific status by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Status at provided id was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatusDtoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Status with the given ID was not found", content = @Content)})
    ResponseEntity<StatusDtoResponse> getStatusById(@PathVariable Long id) {
        return statusService.getStatusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add new status")
    @ApiResponse(responseCode = "201",
            description = "New status has added",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = StatusDtoRequest.class))})

    ResponseEntity<StatusDtoResponse> save(@Valid @RequestBody StatusDtoRequest statusDtoRequest) {
        StatusDtoResponse savedStatus = statusService.saveStatus(statusDtoRequest);
        URI savedStatusUri = UriHelper.getUri(savedStatus.getId());
        return ResponseEntity.created(savedStatusUri).body(savedStatus);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific status by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Status successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Status with the given ID was not found",
                    content = @Content)})
    ResponseEntity<?> replaceStatus(@PathVariable Long id, @Valid @RequestBody StatusDtoRequest statusDtoRequest) {
        return statusService.replaceStatus(id, statusDtoRequest)
                .map(statusDtoResponse -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}
