package pl.pawsko.toolroom.location;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
    public Iterable<LocationDto> getAll() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific location byID",summary = "Get specific location byID")
    public ResponseEntity<LocationDto> getById (@PathVariable Long id) {
        return locationService.getLocationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add new location", summary = "Add new location")
    ResponseEntity<LocationDto> saveCategory(@RequestBody LocationDto locationDto) {
        LocationDto savedLocation = locationService.saveLocation(locationDto);
        URI savedLocationUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedLocation.getId())
                .toUri();
        return ResponseEntity.created(savedLocationUri).body(savedLocation);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific location byID", summary = "Edit specific location byID")
    ResponseEntity<?> replaceCategory(@PathVariable Long id, @RequestBody LocationDto locationDto) {
        return locationService.replaceLocation(id, locationDto)
                .map(c -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
