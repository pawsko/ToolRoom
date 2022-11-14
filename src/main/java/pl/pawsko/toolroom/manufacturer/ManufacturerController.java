package pl.pawsko.toolroom.manufacturer;

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
@Tag(name = "Manufacturers")
@RequestMapping("/api/manufacturer")
class ManufacturerController {
    private final ManufacturerService manufacturerService;

    @GetMapping
    @Operation(description = "Get all manufacturers")
    @ApiResponse(responseCode = "200", description = "List of all manufacturers", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ManufacturerDtoResponse.class)))})
    List<ManufacturerDtoResponse> getAllManufacturers() {
        return manufacturerService.getAllManufactures();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific manufacturer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Manufacturer at provided id was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ManufacturerDtoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Manufacturer with the given ID was not found", content = @Content)})
    ResponseEntity<ManufacturerDtoResponse> getManufacturerById(@PathVariable Long id) {
        return manufacturerService.findManufacturerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add new manufacturer")
    @ApiResponse(responseCode = "201",
            description = "New location has added",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ManufacturerDtoRequest.class))})

    ResponseEntity<ManufacturerDtoResponse> saveManufacturer(@Valid @RequestBody ManufacturerDtoRequest manufacturerDtoRequest) {
        ManufacturerDtoResponse savedManufacturer = manufacturerService.saveManufacturer(manufacturerDtoRequest);
        URI savedManufacturerUri = UriHelper.getUri(savedManufacturer.getId());
        return ResponseEntity.created(savedManufacturerUri).body(savedManufacturer);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific manufacturer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Manufacturer successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Manufacturer with the given ID was not found",
                    content = @Content)})
    ResponseEntity<?> replaceManufacturer(@PathVariable Long id, @Valid @RequestBody ManufacturerDtoRequest manufacturerDtoRequest) {
        return manufacturerService.replaceManufacturer(id, manufacturerDtoRequest)
                .map(manufacturerDtoResponse -> ResponseEntity.noContent().build())
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
