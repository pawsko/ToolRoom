package pl.pawsko.toolroom.tool;

import lombok.Data;
import pl.pawsko.toolroom.category.Category;
import pl.pawsko.toolroom.location.Location;
import pl.pawsko.toolroom.manufacturer.Manufacturer;
import pl.pawsko.toolroom.powertype.PowerType;
import pl.pawsko.toolroom.status.Status;

@Data
class ToolDtoResponse {
    private Long id;
    private String name;
    private String model;
    private Manufacturer manufacturer;
    private Category category;
    private PowerType powerType;
    private Status status;
    private Location location;
}
