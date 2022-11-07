package pl.pawsko.toolroom.tool;

import lombok.Data;

@Data
public class ToolDtoRequest {
    private String name;
    private String model;
    private Long manufacturerId;
    private Long categoryId;
    private Long powerTypeId;
    private Long statusId;
    private int rating;
    private Long locationId;
}
