package pl.pawsko.toolroom.rental;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.pawsko.toolroom.tool.Tool;
import pl.pawsko.toolroom.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Entity
@Table(name = "rentals")
@Data
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime rented;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime returned;
    private String notices;
    @ManyToOne
    private User user;
    @ManyToOne
    private Tool tool;

    @PrePersist
    public void prePersist() {
        rented = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    @PreUpdate
    public void preUpdate() {
        returned = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

}
