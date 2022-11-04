package pl.pawsko.toolroom.status;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StatusService {
    private final StatusRepository statusRepository;
    private final StatusDtoMapper statusDtoMapper;

    public StatusService(StatusRepository statusRepository, StatusDtoMapper statusDtoMapper) {
        this.statusRepository = statusRepository;
        this.statusDtoMapper = statusDtoMapper;
    }

    public List<StatusDto> getAllStatuses() {
        return StreamSupport.stream(statusRepository.findAll().spliterator(), false)
                .map(statusDtoMapper::map)
                .collect(Collectors.toList());
    }

    public Optional<StatusDto> getStatusById(Long id) {
        return statusRepository.findById(id)
                .map(statusDtoMapper::map);
    }

    public StatusDto saveStatus(StatusDto statusDto) {
        Status status = statusDtoMapper.map(statusDto);
        Status savedStatus = statusRepository.save(status);
        return statusDtoMapper.map(savedStatus);
    }

    public Optional<StatusDto> replaceStatus(Long id, StatusDto statusDto) {
        if (!statusRepository.existsById(id)) {
            return Optional.empty();
        } else {
            statusDto.setId(id);
            Status statusToUpdate = statusDtoMapper.map(statusDto);
            Status updatedEntity = statusRepository.save(statusToUpdate);
            return Optional.of(statusDtoMapper.map(updatedEntity));
        }
    }
}
