package pl.pawsko.toolroom.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pl.pawsko.toolroom.category.CategoryRepository;
import pl.pawsko.toolroom.location.LocationRepository;
import pl.pawsko.toolroom.manufacturer.Manufacturer;
import pl.pawsko.toolroom.manufacturer.ManufacturerRepository;
import pl.pawsko.toolroom.powertype.PowerTypeRepository;
import pl.pawsko.toolroom.status.StatusRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

@Service
public class ToolService {
    private final ToolRepository toolRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;
    private final PowerTypeRepository powerTypeRepository;
    private final StatusRepository statusRepository;
    private final LocationRepository locationRepository;
    private final ToolDtoMapper toolDtoMapper;

    private ToolDtoResponse toolDtoResponseNotValidated = new ToolDtoResponse();

    public ToolService(ToolRepository toolRepository, ManufacturerRepository manufacturerRepository, CategoryRepository categoryRepository, PowerTypeRepository powerTypeRepository, StatusRepository statusRepository, LocationRepository locationRepository, ToolDtoMapper toolDtoMapper) {
        this.toolRepository = toolRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.categoryRepository = categoryRepository;
        this.powerTypeRepository = powerTypeRepository;
        this.statusRepository = statusRepository;
        this.locationRepository = locationRepository;
        this.toolDtoMapper = toolDtoMapper;
    }


    public List<ToolDtoResponse> getAllTools() {
        return StreamSupport.stream(toolRepository.findAll().spliterator(), false)
                .map(toolDtoMapper::map)
                .collect(Collectors.toList());
    }

    public Optional<ToolDtoResponse> getToolById(Long id) {
        return toolRepository.findById(id)
                .map(toolDtoMapper::map);
    }

    public ToolDtoResponse saveTool(ToolDtoRequest toolDtoRequest) {
        List<String> validationResult = validateRequest(toolDtoRequest);
        if (validationResult.isEmpty()){
            Tool tool = toolDtoMapper.map(toolDtoRequest);
            Tool savedTool = toolRepository.save(tool);
            return toolDtoMapper.map(savedTool);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(validationResult);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        throw new IllegalArgumentException(validationResult.toString());
        throw new IllegalArgumentException(json);
    }

    private List<String> validateRequest(ToolDtoRequest toolDtoRequest) {
        List<String> validationErrors = new ArrayList<>();
        if (!manufacturerRepository.existsById(toolDtoRequest.getManufacturerId())) {
            validationErrors.add(String.format("Manufacturer Id=%s doesn't exist", toolDtoRequest.getManufacturerId()));
        }
        if (!categoryRepository.existsById(toolDtoRequest.getCategoryId())) {
            validationErrors.add(String.format("Category Id=%s doesn't exist", toolDtoRequest.getCategoryId()));
        }
        if (!powerTypeRepository.existsById(toolDtoRequest.getPowerTypeId())) {
            validationErrors.add(String.format("Power type Id=%s doesn't exist", toolDtoRequest.getPowerTypeId()));
        }
        if (!statusRepository.existsById(toolDtoRequest.getStatusId())) {
            validationErrors.add(String.format("Status Id=%s doesn't exist", toolDtoRequest.getStatusId()));
        }
        if (!locationRepository.existsById(toolDtoRequest.getLocationId())) {
            validationErrors.add(String.format("Location Id=%s doesn't exist", toolDtoRequest.getLocationId()));
        }
        return validationErrors;
    }

    public Optional<ToolDtoResponse> replaceTool(Long id, ToolDtoRequest toolDtoRequest) {
        if (!toolRepository.existsById(id)) {
            return Optional.empty();
        } else {
            Optional<Tool> byId = toolRepository.findById(id);
            Tool toolToUpdate = toolDtoMapper.map(toolDtoRequest);
            toolToUpdate.setId(id);
            toolToUpdate.setCreated(byId.orElseThrow().getCreated());
            Tool updatedEntity = toolRepository.save(toolToUpdate);
            return Optional.of(toolDtoMapper.map(updatedEntity));
        }
    }
}
