package pl.pawsko.toolroom.rental;

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
@Tag(name = "Rentals")
@RequestMapping("/api/rental")
public class RentalController {
    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    @Operation(description = "Get all rentals")
    @ApiResponse(responseCode = "200", description = "List of all rentals", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = RentalDtoResponse.class)))})
    List<RentalDtoResponse> getAll() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific rental by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Rental at provided id was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalDtoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "The rental with the given ID was not found", content = @Content)})
    ResponseEntity<RentalDtoResponse> getRentalById(@PathVariable Long id) {
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
    ResponseEntity<RentalDtoResponse> saveRental(@RequestBody RentalDtoRequest rentalDtoRequest) {
        RentalDtoResponse savedRental = rentalService.saveRental(rentalDtoRequest);
        URI savedRentalUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(savedRental.getId())
                .toUri();
        return ResponseEntity.created(savedRentalUri).body(savedRental);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific rental by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rental successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "The rental with the given ID was not found",
                    content = @Content)})
    ResponseEntity<?> replaceRental(@PathVariable Long id, @RequestBody RentalDtoRequest rentalDtoRequest) {
        return rentalService.replaceRental(id, rentalDtoRequest)
                .map(rentalDtoResponse -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
