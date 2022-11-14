package pl.pawsko.toolroom.manufacturer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
class ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;
    private final ManufacturerDtoMapper manufacturerDtoMapper;

    List<ManufacturerDtoResponse> getAllManufactures() {
        return StreamSupport.stream(manufacturerRepository.findAll().spliterator(),false)
                .map(manufacturerDtoMapper::map)
                .collect(Collectors.toList());
    }

    Optional<ManufacturerDtoResponse> findManufacturerById(Long id) {
        return manufacturerRepository.findById(id)
                .map(manufacturerDtoMapper::map);
    }

    ManufacturerDtoResponse saveManufacturer(ManufacturerDtoRequest manufacturerDtoRequest) {
        Manufacturer manufacturer = manufacturerDtoMapper.map(manufacturerDtoRequest);
        Manufacturer savedManufacturer = manufacturerRepository.save(manufacturer);
        return manufacturerDtoMapper.map(savedManufacturer);
    }

    Optional<ManufacturerDtoResponse> replaceManufacturer(Long id, ManufacturerDtoRequest manufacturerDtoRequest) {
        if (!manufacturerRepository.existsById(id)) {
            return Optional.empty();
        } else {
            Manufacturer manufacturerToUpdate = manufacturerDtoMapper.map(manufacturerDtoRequest);
            manufacturerToUpdate.setId(id);
            Manufacturer updatedEntity = manufacturerRepository.save(manufacturerToUpdate);
            return Optional.of(manufacturerDtoMapper.map(updatedEntity));
        }
    }
}

