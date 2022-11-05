package pl.pawsko.toolroom.status;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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
    List<StatusDto> getAll() {
        return statusService.getAllStatuses();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific status byID", summary = "Get specific status byID")
    ResponseEntity<StatusDto> getById(@PathVariable Long id) {
        return statusService.getStatusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add new status", summary = "Add new status")
    ResponseEntity<StatusDto> save(@RequestBody StatusDto statusDto) {
        StatusDto savedStatus = statusService.saveStatus(statusDto);
        URI savedLocationUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedStatus.getId())
                .toUri();
        return ResponseEntity.created(savedLocationUri).body(savedStatus);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific status byID", summary = "Edit specific status byID")
    ResponseEntity<?> replace(@PathVariable Long id, @RequestBody StatusDto statusDto) {
        return statusService.replaceStatus(id, statusDto)
                .map(c -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
