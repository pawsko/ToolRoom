package pl.pawsko.toolroom.rental;

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
@Tag(name = "Rentals")
@RequestMapping("/api/rental")
class RentalController {
    private final RentalService rentalService;

    @GetMapping
    @Operation(description = "Get all rentals")
    @ApiResponse(responseCode = "200", description = "List of all rentals", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = RentalDtoResponse.class)))})
    List<RentalDtoResponse> getAllRentals() {
        log.debug("Getting all rentals");
        return rentalService.getAllRentals();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific rental by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Rental at provided id was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalDtoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Rental with the given ID was not found", content = @Content)})
    ResponseEntity<RentalDtoResponse> getRentalById(@PathVariable Long id) {
        log.debug("Getting rental by id={}", id);
        return rentalService.getRentalById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add new rental")
    @ApiResponse(responseCode = "201",
            description = "New rental has added",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = RentalDtoRequest.class))})
    ResponseEntity<RentalDtoResponse> saveRental(@Valid @RequestBody RentalDtoRequest rentalDtoRequest) {
        RentalDtoResponse savedRental = rentalService.saveRental(rentalDtoRequest);
        URI savedRentalUri = UriHelper.getUri(savedRental.getId());
        log.debug("Saved new rental {}", savedRental);
        return ResponseEntity.created(savedRentalUri).body(savedRental);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific rental by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rental successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Rental with the given ID was not found",
                    content = @Content)})
    ResponseEntity<?> replaceRental(@PathVariable Long id, @Valid @RequestBody RentalDtoRequest rentalDtoRequest) {
        log.debug("Replaced rental id={}", id);
        return rentalService.replaceRental(id, rentalDtoRequest)
                .map(rentalDtoResponse -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Bad request 400");
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}
