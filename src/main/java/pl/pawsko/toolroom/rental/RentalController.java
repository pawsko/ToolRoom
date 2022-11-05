package pl.pawsko.toolroom.rental;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@Tag(name = "Rentals")
@RequestMapping("/api/rental")
public class RentalController {
    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    @Operation(description = "Get all rentals", summary = "Get all rentals")
    List<RentalDto> getAll() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific rental byID",summary = "Get specific rental byID")
    ResponseEntity<RentalDto> getRentalById(@PathVariable Long id) {
        return rentalService.getRentalById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add new rental", summary = "Add new rental")
    ResponseEntity<RentalDto> saveRental(@RequestBody RentalDto rentalDto) {
        RentalDto savedRental = rentalService.saveRental(rentalDto);
        URI savedRentalUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(savedRental.getId())
                .toUri();
        return ResponseEntity.created(savedRentalUri).body(savedRental);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific rental byID", summary = "Edit specific rental byID")
    ResponseEntity<?> replaceRental(@PathVariable Long id, @RequestBody RentalDto rentalDto) {
        return rentalService.replaceRental(id, rentalDto)
                .map(r -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
