package pl.pawsko.toolroom.rental;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pl.pawsko.toolroom.rental.validators.ToolConstraint;
import pl.pawsko.toolroom.rental.validators.UserConstraint;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
class RentalDtoRequest {
    @ToolConstraint
    private Long toolId;
    @UserConstraint
    private Long userId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime rented;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime returned;
    private String notices;
}
