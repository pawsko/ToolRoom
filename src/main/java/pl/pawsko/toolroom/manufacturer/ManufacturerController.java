package pl.pawsko.toolroom.manufacturer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/manufacturer")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping
    public List<ManufacturerDto> getAll() {
        return manufacturerService.getAllManufactures();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerDto> getById(@PathVariable Long id) {
        return manufacturerService.findManufacturerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<ManufacturerDto> saveManufacturer(@RequestBody ManufacturerDto manufacturerDto) {
        ManufacturerDto saveManufacturer = manufacturerService.saveManufacturer(manufacturerDto);
        URI savedLocationUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saveManufacturer.getId())
                .toUri();
        return ResponseEntity.created(savedLocationUri).body(saveManufacturer);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> replaceCategory(@PathVariable Long id, @RequestBody ManufacturerDto manufacturerDto) {
        return manufacturerService.replaceManufacturer(id, manufacturerDto)
                .map(c -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
