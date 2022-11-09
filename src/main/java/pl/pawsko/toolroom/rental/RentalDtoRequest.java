package pl.pawsko.toolroom.rental;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.pawsko.toolroom.rental.validators.ToolConstraint;
import pl.pawsko.toolroom.rental.validators.UserConstraint;

import java.time.LocalDateTime;

@Data
public class RentalDtoRequest {
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
