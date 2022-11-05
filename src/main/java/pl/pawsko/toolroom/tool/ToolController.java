package pl.pawsko.toolroom.tool;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;


@RestController
@Tag(name = "Tools")
@RequestMapping("/api/tool")
public class ToolController {
    private final ToolService toolService;

    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }


    @GetMapping
    @Operation(description = "Get all tools", summary = "Get all tools")
    public Iterable<ToolDto> getAllTools() {
        return toolService.getAllTools();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific tool byID",summary = "Get specific tool byID")
    public ResponseEntity<ToolDto> getToolById(@PathVariable Long id) {
        return toolService.getToolById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add new tool", summary = "Add new tool")
    ResponseEntity<ToolDto> saveTool(@RequestBody ToolDto toolDto) {
        ToolDto savedUser = toolService.saveTool(toolDto);
        URI savedUserUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(savedUserUri).body(savedUser);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific tool byID", summary = "Edit specific tool byID")
    ResponseEntity<?> replaceTool(@PathVariable Long id, @RequestBody ToolDto toolDto) {
        return toolService.replaceTool(id, toolDto)
                .map(c -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}

