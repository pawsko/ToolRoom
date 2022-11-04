package pl.pawsko.toolroom.manufacturer;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;
    private final ManufacturerDtoMapper manufacturerDtoMapper;

    public ManufacturerService(ManufacturerRepository manufacturerRepository, ManufacturerDtoMapper manufacturerDtoMapper) {
        this.manufacturerRepository = manufacturerRepository;
        this.manufacturerDtoMapper = manufacturerDtoMapper;
    }

    public List<ManufacturerDto> getAllManufactures() {
        return StreamSupport.stream(manufacturerRepository.findAll().spliterator(),false)
                .map(manufacturerDtoMapper::map)
                .collect(Collectors.toList());
    }

    public Optional<ManufacturerDto> findManufacturerById(Long id) {
        return manufacturerRepository.findById(id)
                .map(manufacturerDtoMapper::map);
    }

    public ManufacturerDto saveManufacturer(ManufacturerDto manufacturerDto) {
        Manufacturer manufacturer = manufacturerDtoMapper.map(manufacturerDto);
        Manufacturer savedManufacturer = manufacturerRepository.save(manufacturer);
        return manufacturerDtoMapper.map(savedManufacturer);
    }

    public Optional<ManufacturerDto> replaceManufacturer(Long id, ManufacturerDto manufacturerDto) {
        if (!manufacturerRepository.existsById(id)) {
            return Optional.empty();
        } else {
            manufacturerDto.setId(id);
            Manufacturer manufacturerToUpdate = manufacturerDtoMapper.map(manufacturerDto);
            Manufacturer updatedEntity = manufacturerRepository.save(manufacturerToUpdate);
            return Optional.of(manufacturerDtoMapper.map(updatedEntity));
        }
    }
}

