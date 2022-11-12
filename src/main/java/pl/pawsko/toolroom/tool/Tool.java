package pl.pawsko.toolroom.tool;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import pl.pawsko.toolroom.category.Category;
import pl.pawsko.toolroom.location.Location;
import pl.pawsko.toolroom.manufacturer.Manufacturer;
import pl.pawsko.toolroom.powertype.PowerType;
import pl.pawsko.toolroom.status.Status;
import pl.pawsko.toolroom.user.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "tools")
@Data
public class Tool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String model;
    @ManyToOne
    private Manufacturer manufacturer;
    @ManyToOne
    private Category category;    //Hand tools, Power tools, Petrol tools, Measuring tools
    @ManyToOne
    private PowerType powerType; //if applicable 230, battery, gasoline for 4T, gasoline + oilmix for 2T handy
    @ManyToOne
    private Status status; //Available, not available, damaged, in service, out of ofrder
    private int rating; //included accuracy, rate of wear and tear
    @ManyToOne
    private Location location;
    @ManyToOne
    private User user;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime created;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime lastUpdate;

    @PrePersist
    public void prePersist() {
        created = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

}


