package pl.pawsko.toolroom.powertype;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Power Types")
@RequestMapping("/api/powertype")
class PowerTypeController {
    private final PowerTypeService powerTypeService;

    @GetMapping
    @Operation(description = "Get all power types")
    @ApiResponse(responseCode = "200", description = "List of all power types", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = PowerTypeDtoResponse.class)))})
    List<PowerTypeDtoResponse> getAllPowerTypes() {
        log.debug("Getting all power types");
        return powerTypeService.getAllPowerTypes();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific power type by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Power type at provided id was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PowerTypeDtoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Power type with the given ID was not found", content = @Content)})
    ResponseEntity<PowerTypeDtoResponse> getPowerTypeById(@PathVariable Long id) {
        log.debug("Getting power type by id={}", id);
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

    ResponseEntity<PowerTypeDtoResponse> save(@Valid @RequestBody PowerTypeDtoRequest powerTypeDtoRequest) {
        PowerTypeDtoResponse savedPowerType = powerTypeService.savePowerType(powerTypeDtoRequest);
        URI savedPowerTypeUri = UriHelper.getUri(savedPowerType.getId());
        log.debug("Saved new power type {}", savedPowerType);
        return ResponseEntity.created(savedPowerTypeUri).body(savedPowerType);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific power type by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Power type successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Power type with the given ID was not found",
                    content = @Content)})
    ResponseEntity<?> replacePowerType(@PathVariable Long id, @Valid @RequestBody PowerTypeDtoRequest powerTypeDtoRequest) {
        log.debug("Replaced power type id={}", id);
        return powerTypeService.replacePowerType(id, powerTypeDtoRequest)
                .map(powerTypeDtoResponse -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.debug("Bad request 400");
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}
