package pl.pawsko.toolroom.tool;

import lombok.Data;
import pl.pawsko.toolroom.category.Category;
import pl.pawsko.toolroom.location.Location;
import pl.pawsko.toolroom.manufacturer.Manufacturer;
import pl.pawsko.toolroom.powertype.PowerType;
import pl.pawsko.toolroom.status.Status;

import java.time.LocalDateTime;

@Data
public class ToolDto {
    private Long id;
    private String name;
    private String model;
    private Manufacturer manufacturer;
    private Category category;
    private PowerType powerType;
    private Status status;
    private int rating;
    private Location location;
    private LocalDateTime created;
    private LocalDateTime lastUpdate;
}
