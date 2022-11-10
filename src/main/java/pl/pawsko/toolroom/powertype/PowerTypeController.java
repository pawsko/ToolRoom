package pl.pawsko.toolroom.powertype;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    @Operation(description = "Get all power types")
    @ApiResponse(responseCode = "200", description = "List of all power types", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = PowerTypeDtoResponse.class)))})
    public List<PowerTypeDtoResponse> getAll() {
        return powerTypeService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific power type by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Power type at provided id was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PowerTypeDtoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "The power type with the given ID was not found", content = @Content)})
    public ResponseEntity<PowerTypeDtoResponse> getById(@PathVariable Long id) {
        return powerTypeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add new power type")
    @ApiResponse(responseCode = "201",
            description = "New power type has added",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = PowerTypeDtoRequest.class))})
    ResponseEntity<PowerTypeDtoResponse> save(@RequestBody PowerTypeDtoRequest powerTypeDtoRequest) {
        PowerTypeDtoResponse savedPowerType = powerTypeService.savePowerType(powerTypeDtoRequest);
        URI savedPowerTypeUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPowerType.getId())
                .toUri();
        return ResponseEntity.created(savedPowerTypeUri).body(savedPowerType);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific power type by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Power type successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "The power type with the given ID was not found",
                    content = @Content)})
    ResponseEntity<?> replace(@PathVariable Long id, @RequestBody PowerTypeDtoRequest powerTypeDtoRequest) {
        return powerTypeService.replacePowerType(id, powerTypeDtoRequest)
                .map(powerTypeDtoResponse -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
