package pl.pawsko.toolroom.status;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/status")
public class StatusController {
    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    Iterable<StatusDto> getAll() {
        return statusService.getAllStatuses();
    }

    @GetMapping("/{id}")
    ResponseEntity<StatusDto> getById(@PathVariable Long id) {
        return statusService.getStatusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<StatusDto> save(@RequestBody StatusDto statusDto) {
        StatusDto savedStatus = statusService.saveStatus(statusDto);
        URI savedLocationUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedStatus.getId())
                .toUri();
        return ResponseEntity.created(savedLocationUri).body(savedStatus);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> replace(@PathVariable Long id, @RequestBody StatusDto statusDto) {
        return statusService.replaceStatus(id, statusDto)
                .map(c -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
