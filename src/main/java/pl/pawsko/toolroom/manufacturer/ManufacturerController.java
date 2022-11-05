package pl.pawsko.toolroom.manufacturer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "Manufacturers")
@RequestMapping("/api/manufacturer")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping
    @Operation(description = "Get all manufacturers", summary = "Get all manufacturers")
    public Iterable<ManufacturerDto> getAll() {
        return manufacturerService.getAllManufactures();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific manufacturer byID",summary = "Get specific manufacturer byID")
    public ResponseEntity<ManufacturerDto> getById(@PathVariable Long id) {
        return manufacturerService.findManufacturerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add new manufacturer", summary = "Add new manufacturer")
    ResponseEntity<ManufacturerDto> saveManufacturer(@RequestBody ManufacturerDto manufacturerDto) {
        ManufacturerDto saveManufacturer = manufacturerService.saveManufacturer(manufacturerDto);
        URI savedLocationUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saveManufacturer.getId())
                .toUri();
        return ResponseEntity.created(savedLocationUri).body(saveManufacturer);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific manufacturer byID", summary = "Edit specific manufacturer byID")
    ResponseEntity<?> replaceCategory(@PathVariable Long id, @RequestBody ManufacturerDto manufacturerDto) {
        return manufacturerService.replaceManufacturer(id, manufacturerDto)
                .map(c -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
