package pl.pawsko.toolroom.tool;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import pl.pawsko.toolroom.tool.validators.CategoryCustomConstraint;
import pl.pawsko.toolroom.tool.validators.LocationCustomConstraint;
import pl.pawsko.toolroom.tool.validators.ManufacturerCustomConstraint;
import pl.pawsko.toolroom.tool.validators.PowerTypeCustomConstraint;
import pl.pawsko.toolroom.tool.validators.StatusCustomConstraint;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class ToolDtoRequest {
    @Size(min = 3, message = "Tool name must contain minimum {min} signs")
    private String name;
    private String model;
    @ManufacturerCustomConstraint
    private Long manufacturerId;
    @CategoryCustomConstraint
    private Long categoryId;
    @PowerTypeCustomConstraint
    private Long powerTypeId;
    @StatusCustomConstraint
    private Long statusId;
    @Schema(minimum = "0", maximum = "10", description = "rating 0-10")
    @Min(value = 0, message = "Rating must be at least {value}")
    @Max(value = 10, message = "Rating must be equal or less then {value}")
    private int rating;
    @LocationCustomConstraint
    private Long locationId;
}
