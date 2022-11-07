package pl.pawsko.toolroom.tool;

import org.springframework.stereotype.Service;
import pl.pawsko.toolroom.category.Category;
import pl.pawsko.toolroom.location.Location;
import pl.pawsko.toolroom.manufacturer.Manufacturer;
import pl.pawsko.toolroom.powertype.PowerType;
import pl.pawsko.toolroom.status.Status;

@Service
public class ToolDtoMapper {
    ToolDtoResponse map(Tool tool) {
        ToolDtoResponse dto = new ToolDtoResponse();
        dto.setId(tool.getId());
        dto.setName(tool.getName());
        dto.setModel(tool.getModel());
        dto.setManufacturer(tool.getManufacturer());
        dto.setCategory(tool.getCategory());
        dto.setPowerType(tool.getPowerType());
        dto.setStatus(tool.getStatus());
        dto.setLocation(tool.getLocation());
        return dto;
    }

    Tool map(ToolDtoRequest toolDtoRequest) {
        Tool tool = new Tool();
        tool.setName(toolDtoRequest.getName());
        tool.setModel(toolDtoRequest.getModel());
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(toolDtoRequest.getManufacturerId());
        tool.setManufacturer(manufacturer);
        Category category = new Category();
        category.setId(toolDtoRequest.getCategoryId());
        tool.setCategory(category);
        PowerType powerType = new PowerType();
        powerType.setId(toolDtoRequest.getPowerTypeId());
        tool.setPowerType(powerType);
        Status status = new Status();
        status.setId(toolDtoRequest.getStatusId());
        tool.setStatus(status);
        tool.setRating(toolDtoRequest.getRating());
        Location location = new Location();
        location.setId(toolDtoRequest.getLocationId());
        tool.setLocation(location);
        return tool;
    }

}
