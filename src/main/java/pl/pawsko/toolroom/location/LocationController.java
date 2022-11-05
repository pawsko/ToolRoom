package pl.pawsko.toolroom.location;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "Locations")
@RequestMapping("/api/location")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    @Operation(description = "Get all locations", summary = "Get all locations")
    @ApiResponse(responseCode = "200", description = "List of all locations", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = LocationDto.class)))})
    public List<LocationDto> getAll() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific location by id", summary = "Get specific location by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Location at provided id was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LocationDto.class))}),
            @ApiResponse(responseCode = "404", description = "The location with the given ID was not found", content = @Content)})
    public ResponseEntity<LocationDto> getById(@PathVariable Long id) {
        return locationService.getLocationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add new location", summary = "Add new location")
    @ApiResponse(responseCode = "201",
            description = "New location has added",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = LocationDto.class))})
    ResponseEntity<LocationDto> saveCategory(@RequestBody LocationDto locationDto) {
        LocationDto savedLocation = locationService.saveLocation(locationDto);
        URI savedLocationUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedLocation.getId())
                .toUri();
        return ResponseEntity.created(savedLocationUri).body(savedLocation);
    }

    @PutMapping("/{id}")
    @Operation(description = "Updates the location with the given id", summary = "Updates the location with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "The location with the given ID was not found",
                    content = @Content)})
    ResponseEntity<?> replaceCategory(@PathVariable Long id, @RequestBody LocationDto locationDto) {
        return locationService.replaceLocation(id, locationDto)
                .map(l -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
