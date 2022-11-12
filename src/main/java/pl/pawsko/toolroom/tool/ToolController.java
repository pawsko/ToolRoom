package pl.pawsko.toolroom.tool;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.pawsko.toolroom.hellpers.UriHelper;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Tools")
@RequestMapping("/api/tool")
public class ToolController {
    private final ToolService toolService;

    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }


    @GetMapping
    @Operation(description = "Get all tools")
    @ApiResponse(responseCode = "200", description = "List of all tools", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ToolDtoResponse.class)))})
    public List<ToolDtoResponse> getAllTools() {
        return toolService.getAllTools();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific tool by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Tool at provided id was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ToolDtoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Tool with the given ID was not found", content = @Content)})
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

    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> saveTool(@Valid @RequestBody ToolDtoRequest toolDtoRequest) {
        ToolDtoResponse savedTool = toolService.saveTool(toolDtoRequest);
        URI savedToolUri = UriHelper.getUri(savedTool.getId());
            return ResponseEntity.created(savedToolUri).body(savedTool);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific tool by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tool successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Tool with the given ID was not found",
                    content = @Content)})
    ResponseEntity<?> replaceTool(@PathVariable Long id, @Valid @RequestBody ToolDtoRequest toolDtoRequest) {
        return toolService.replaceTool(id, toolDtoRequest)
                .map(toolDtoResponse -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}

