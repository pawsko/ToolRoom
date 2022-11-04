package pl.pawsko.toolroom.tool;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ToolService {
    private final ToolRepository toolRepository;
    private final ToolDtoMapper toolDtoMapper;

    public ToolService(ToolRepository toolRepository, ToolDtoMapper toolDtoMapper) {
        this.toolRepository = toolRepository;
        this.toolDtoMapper = toolDtoMapper;
    }


    public List<ToolDto> getAllTools() {
        return StreamSupport.stream(toolRepository.findAll().spliterator(), false)
                .map(toolDtoMapper::map)
                .collect(Collectors.toList());
    }

    public Optional<ToolDto> getToolById(Long id) {
        return toolRepository.findById(id)
                .map(toolDtoMapper::map);
    }

    public ToolDto saveTool(ToolDto toolDto) {
        Tool tool = toolDtoMapper.map(toolDto);
        Tool savedTool = toolRepository.save(tool);
        return toolDtoMapper.map(savedTool);
    }

    public Optional<ToolDto> replaceTool(Long id, ToolDto toolDto) {
        if (!toolRepository.existsById(id)) {
            return Optional.empty();
        } else {
            Optional<Tool> byId = toolRepository.findById(id);
            toolDto.setId(id);
            Tool toolToUpdate = toolDtoMapper.map(toolDto);
            toolToUpdate.setCreated(byId.orElseThrow().getCreated());
            Tool updatedEntity = toolRepository.save(toolToUpdate);
            return Optional.of(toolDtoMapper.map(updatedEntity));
        }
    }
}
