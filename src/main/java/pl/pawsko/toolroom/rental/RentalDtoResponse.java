package pl.pawsko.toolroom.rental;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.pawsko.toolroom.tool.Tool;
import pl.pawsko.toolroom.user.User;

import java.time.LocalDateTime;

@Data
class RentalDtoResponse {
    private Long id;
    private Tool tool;
    private User user;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime rented;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime returned;
    private String notices;
}
