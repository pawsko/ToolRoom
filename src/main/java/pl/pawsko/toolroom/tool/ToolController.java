package pl.pawsko.toolroom.tool;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/tool")
public class ToolController {
    private final ToolService toolService;

    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }


    @GetMapping
    public List<ToolDto> getAllTools() {
        return toolService.getAllTools();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToolDto> getToolById(@PathVariable Long id) {
        return toolService.getToolById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<ToolDto> saveTool(@RequestBody ToolDto toolDto) {
        ToolDto savedUser = toolService.saveTool(toolDto);
        URI savedUserUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(savedUserUri).body(savedUser);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> replaceTool(@PathVariable Long id, @RequestBody ToolDto toolDto) {
        return toolService.replaceTool(id, toolDto)
                .map(c -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}

