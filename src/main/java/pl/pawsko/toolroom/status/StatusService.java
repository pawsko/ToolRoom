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

    public List<StatusDtoResponse> getAllStatuses() {
        return StreamSupport.stream(statusRepository.findAll().spliterator(), false)
                .map(statusDtoMapper::map)
                .collect(Collectors.toList());
    }

    public Optional<StatusDtoResponse> getStatusById(Long id) {
        return statusRepository.findById(id)
                .map(statusDtoMapper::map);
    }

    public StatusDtoResponse saveStatus(StatusDtoRequest statusDtoRequest) {
        Status status = statusDtoMapper.map(statusDtoRequest);
        Status savedStatus = statusRepository.save(status);
        return statusDtoMapper.map(savedStatus);
    }

    public Optional<StatusDtoResponse> replaceStatus(Long id, StatusDtoRequest statusDtoRequest) {
        if (!statusRepository.existsById(id)) {
            return Optional.empty();
        } else {
            Status statusToUpdate = statusDtoMapper.map(statusDtoRequest);
            statusToUpdate.setId(id);
            Status updatedEntity = statusRepository.save(statusToUpdate);
            return Optional.of(statusDtoMapper.map(updatedEntity));
        }
    }
}
