package pl.pawsko.toolroom.status;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
class StatusService {
    private final StatusRepository statusRepository;
    private final StatusDtoMapper statusDtoMapper;

    List<StatusDtoResponse> getAllStatuses() {
        return StreamSupport.stream(statusRepository.findAll().spliterator(), false)
                .map(statusDtoMapper::map)
                .collect(Collectors.toList());
    }

    Optional<StatusDtoResponse> getStatusById(Long id) {
        return statusRepository.findById(id)
                .map(statusDtoMapper::map);
    }

    StatusDtoResponse saveStatus(StatusDtoRequest statusDtoRequest) {
        Status status = statusDtoMapper.map(statusDtoRequest);
        Status savedStatus = statusRepository.save(status);
        return statusDtoMapper.map(savedStatus);
    }

    Optional<StatusDtoResponse> replaceStatus(Long id, StatusDtoRequest statusDtoRequest) {
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
