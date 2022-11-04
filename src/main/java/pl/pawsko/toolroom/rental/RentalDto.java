package pl.pawsko.toolroom.rental;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.pawsko.toolroom.tool.Tool;
import pl.pawsko.toolroom.user.User;

import java.time.LocalDateTime;

@Data
public class RentalDto {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime rented;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime returned;
    private String notices;
    private User user;
    private Tool tool;
}
