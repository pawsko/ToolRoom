package pl.pawsko.toolroom.tool;

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
@Tag(name = "Tools")
@RequestMapping("/api/tool")
public class ToolController {
    private final ToolService toolService;

    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }


    @GetMapping
    @Operation(description = "Get all tools", summary = "Get all tools")
    @ApiResponse(responseCode = "200", description = "List of all tools", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ToolDtoResponse.class)))})
    public List<ToolDtoResponse> getAllTools() {
        return toolService.getAllTools();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific tool by id", summary = "Get specific tool by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Tool at provided id was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ToolDtoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "The tool with the given ID was not found", content = @Content)})
    public ResponseEntity<ToolDtoResponse> getToolById(@PathVariable Long id) {
        return toolService.getToolById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add new tool", summary = "Add new tool")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "New tool has added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ToolDtoRequest.class))}),
            @ApiResponse(responseCode = "400",
                    description = "New tool didn't create because of errors")
    })
    ResponseEntity<?> saveTool(@RequestBody ToolDtoRequest toolDtoRequest) {
        try {
            ToolDtoResponse savedTool = toolService.saveTool(toolDtoRequest);
            URI savedToolUri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedTool.getId())
                    .toUri();
            return ResponseEntity.created(savedToolUri).body(savedTool);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().body(iae.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific tool by id", summary = "Edit specific tool by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tool successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "The tool with the given ID was not found",
                    content = @Content)})
    ResponseEntity<?> replaceTool(@PathVariable Long id, @RequestBody ToolDtoRequest toolDtoRequest) {
        return toolService.replaceTool(id, toolDtoRequest)
                .map(c -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}

