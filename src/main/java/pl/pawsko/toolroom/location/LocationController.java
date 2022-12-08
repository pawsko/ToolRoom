package pl.pawsko.toolroom.location;

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
@Tag(name = "Locations")
@RequestMapping("/api/location")
class LocationController {
    private final LocationService locationService;

    @GetMapping
    @Operation(description = "Get all locations")
    @ApiResponse(responseCode = "200", description = "List of all locations", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = LocationDtoResponse.class)))})
    List<LocationDtoResponse> getAllLocations() {
        log.debug("Getting all locations");
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific location by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Location at provided id was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LocationDtoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Location with the given ID was not found", content = @Content)})
    ResponseEntity<LocationDtoResponse> getLocationById(@PathVariable Long id) {
        log.debug("Getting location by id={}", id);
        return locationService.getLocationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add new location")
    @ApiResponse(responseCode = "201",
            description = "New location has added",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = LocationDtoRequest.class))})
    ResponseEntity<LocationDtoResponse> saveCategory(@Valid @RequestBody LocationDtoRequest locationDtoRequest) {
        LocationDtoResponse savedLocation = locationService.saveLocation(locationDtoRequest);
        URI savedLocationUri = UriHelper.getUri(savedLocation.getId());
        log.debug("Saved new location {}", savedLocation);
        return ResponseEntity.created(savedLocationUri).body(savedLocation);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific location by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Location successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Location with the given ID was not found",
                    content = @Content)})
    ResponseEntity<?> replaceCategory(@PathVariable Long id, @Valid @RequestBody LocationDtoRequest locationDtoRequest) {
        log.debug("Replaced location id={}", id);
        return locationService.replaceLocation(id, locationDtoRequest)
                .map(locationDtoResponse -> ResponseEntity.noContent().build())
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
