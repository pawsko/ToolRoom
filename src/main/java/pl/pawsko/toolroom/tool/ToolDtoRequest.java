package pl.pawsko.toolroom.tool;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ToolDtoRequest {
    private String name;
    private String model;
    private Long manufacturerId;
    private Long categoryId;
    private Long powerTypeId;
    private Long statusId;
    @Schema(minimum = "0", maximum = "10", description = "rating 0-10")
    private int rating;
    private Long locationId;
}
