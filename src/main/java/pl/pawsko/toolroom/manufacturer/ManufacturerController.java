package pl.pawsko.toolroom.manufacturer;

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
@Tag(name = "Manufacturers")
@RequestMapping("/api/manufacturer")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping
    @Operation(description = "Get all manufacturers", summary = "Get all manufacturers")
    @ApiResponse(responseCode = "200", description = "List of all manufacturers", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ManufacturerDtoResponse.class)))})
    public List<ManufacturerDtoResponse> getAll() {
        return manufacturerService.getAllManufactures();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific manufacturer by id",summary = "Get specific manufacturer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Manufacturer at provided id was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ManufacturerDtoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "The manufacturer with the given ID was not found", content = @Content)})
    public ResponseEntity<ManufacturerDtoResponse> getById(@PathVariable Long id) {
        return manufacturerService.findManufacturerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add new manufacturer", summary = "Add new manufacturer")
    @ApiResponse(responseCode = "201",
            description = "New location has added",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ManufacturerDtoRequest.class))})
    ResponseEntity<ManufacturerDtoResponse> saveManufacturer(@RequestBody ManufacturerDtoRequest manufacturerDtoRequest) {
        ManufacturerDtoResponse saveManufacturer = manufacturerService.saveManufacturer(manufacturerDtoRequest);
        URI savedManufacturerUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saveManufacturer.getId())
                .toUri();
        return ResponseEntity.created(savedManufacturerUri).body(saveManufacturer);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific manufacturer by id", summary = "Edit specific manufacturer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Manufacturer successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "The manufacturer with the given ID was not found",
                    content = @Content)})
    ResponseEntity<?> replaceCategory(@PathVariable Long id, @RequestBody ManufacturerDtoRequest manufacturerDtoRequest) {
        return manufacturerService.replaceManufacturer(id, manufacturerDtoRequest)
                .map(c -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
