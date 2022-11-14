package pl.pawsko.toolroom.tool;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
class ToolService {
    private final ToolRepository toolRepository;
    private final ToolDtoMapper toolDtoMapper;

    List<ToolDtoResponse> getAllTools() {
        return StreamSupport.stream(toolRepository.findAll().spliterator(), false)
                .map(toolDtoMapper::map)
                .collect(Collectors.toList());
    }

    Optional<ToolDtoResponse> getToolById(Long id) {
        return toolRepository.findById(id)
                .map(toolDtoMapper::map);
    }

    ToolDtoResponse saveTool(ToolDtoRequest toolDtoRequest) {
            Tool tool = toolDtoMapper.map(toolDtoRequest);
            Tool savedTool = toolRepository.save(tool);
            return toolDtoMapper.map(savedTool);
    }

    Optional<ToolDtoResponse> replaceTool(Long id, ToolDtoRequest toolDtoRequest) {
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
