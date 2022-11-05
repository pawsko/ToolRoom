package pl.pawsko.toolroom.powertype;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "Power Types")
@RequestMapping("/api/powertype")
public class PowerTypeController {
    private final PowerTypeService powerTypeService;

    public PowerTypeController(PowerTypeService powerTypeService) {
        this.powerTypeService = powerTypeService;
    }

    @GetMapping
    @Operation(description = "Get all power types", summary = "Get all power types")
    public List<PowerTypeDto> getAll() {
        return powerTypeService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific power type by id",summary = "Get specific power type by id")
    public ResponseEntity<PowerTypeDto> getById(@PathVariable Long id) {
        return powerTypeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add new power type", summary = "Add new power type")
    ResponseEntity<PowerTypeDto> save(@RequestBody PowerTypeDto powerTypeDto) {
        PowerTypeDto savedPowerType = powerTypeService.savePowerType(powerTypeDto);
        URI savedLocationUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPowerType.getId())
                .toUri();
        return ResponseEntity.created(savedLocationUri).body(savedPowerType);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific power type by id", summary = "Edit specific power type by id")
    ResponseEntity<?> replace(@PathVariable Long id, @RequestBody PowerTypeDto powerTypeDto) {
        return powerTypeService.replacePowerType(id, powerTypeDto)
                .map(c -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
