package pl.pawsko.toolroom.tool;

import org.springframework.stereotype.Service;
import pl.pawsko.toolroom.category.Category;
import pl.pawsko.toolroom.category.CategoryRepository;
import pl.pawsko.toolroom.location.Location;
import pl.pawsko.toolroom.location.LocationRepository;
import pl.pawsko.toolroom.manufacturer.Manufacturer;
import pl.pawsko.toolroom.manufacturer.ManufacturerRepository;
import pl.pawsko.toolroom.powertype.PowerType;
import pl.pawsko.toolroom.powertype.PowerTypeRepository;
import pl.pawsko.toolroom.status.Status;
import pl.pawsko.toolroom.status.StatusRepository;

import java.util.Optional;

@Service
public class ToolDtoMapper {


    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;
    private final PowerTypeRepository powerTypeRepository;
    private final StatusRepository statusRepository;
    private final LocationRepository locationRepository;

    public ToolDtoMapper(ManufacturerRepository manufacturerRepository, CategoryRepository categoryRepository, PowerTypeRepository powerTypeRepository, StatusRepository statusRepository, LocationRepository locationRepository) {
        this.manufacturerRepository = manufacturerRepository;
        this.categoryRepository = categoryRepository;
        this.powerTypeRepository = powerTypeRepository;
        this.statusRepository = statusRepository;
        this.locationRepository = locationRepository;
    }

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
        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(toolDtoRequest.getManufacturerId());
        manufacturer.ifPresent(tool::setManufacturer);
        Optional<Category> category = categoryRepository.findById(toolDtoRequest.getCategoryId());
        category.ifPresent(tool::setCategory);
        Optional<PowerType> powerType = powerTypeRepository.findById(toolDtoRequest.getPowerTypeId());
        powerType.ifPresent(tool::setPowerType);
        Optional<Status> status = statusRepository.findById(toolDtoRequest.getStatusId());
        status.ifPresent(tool::setStatus);
        tool.setRating(toolDtoRequest.getRating());
        Optional<Location> location = locationRepository.findById(toolDtoRequest.getLocationId());
        location.ifPresent(tool::setLocation);
        return tool;
    }

}
