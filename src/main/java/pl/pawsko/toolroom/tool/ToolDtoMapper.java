package pl.pawsko.toolroom.tool;

import org.springframework.stereotype.Service;
import pl.pawsko.toolroom.manufacturer.Manufacturer;

@Service
public class ToolDtoMapper {
    public ToolDto map(Tool tool) {
        ToolDto dto = new ToolDto();
        dto.setId(tool.getId());
        dto.setName(tool.getName());
        dto.setModel(tool.getModel());
        dto.setManufacturer(tool.getManufacturer());
        dto.setCategory(tool.getCategory());
        dto.setPowerType(tool.getPowerType());
        dto.setStatus(tool.getStatus());
        dto.setRating(tool.getRating());
        dto.setLocation(tool.getLocation());
        dto.setCreated(tool.getCreated());
        dto.setLastUpdate(tool.getLastUpdate());
        return dto;
    }

    public Tool map(ToolDto toolDto) {
        Tool tool = new Tool();
        tool.setId(toolDto.getId());
        tool.setName(toolDto.getName());
        tool.setModel(toolDto.getModel());
        tool.setManufacturer(toolDto.getManufacturer());
        tool.setCategory(toolDto.getCategory());
        tool.setPowerType(toolDto.getPowerType());
        tool.setStatus(toolDto.getStatus());
        tool.setRating(toolDto.getRating());
        tool.setLocation(toolDto.getLocation());
        return tool;
    }

}
