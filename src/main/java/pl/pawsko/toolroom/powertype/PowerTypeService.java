package pl.pawsko.toolroom.powertype;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PowerTypeService {
    private final PowerTypeRepository powerTypeRepository;
    private final PowerTypeDtoMapper powerTypeDtoMapper;

    public PowerTypeService(PowerTypeRepository powerTypeRepository, PowerTypeDtoMapper powerTypeDtoMapper) {
        this.powerTypeRepository = powerTypeRepository;
        this.powerTypeDtoMapper = powerTypeDtoMapper;
    }

    public List<PowerTypeDto> getAll() {
        return StreamSupport.stream(powerTypeRepository.findAll().spliterator(), false)
                .map(powerTypeDtoMapper::map)
                .collect(Collectors.toList());
    }

    public Optional<PowerTypeDto> findById(long id) {
        return powerTypeRepository.findById(id)
                .map(powerTypeDtoMapper::map);
    }

    public PowerTypeDto savePowerType(PowerTypeDto powerTypeDto) {
        PowerType powerType = powerTypeDtoMapper.map(powerTypeDto);
        PowerType savedPowerType = powerTypeRepository.save(powerType);
        return powerTypeDtoMapper.map(savedPowerType);
    }

    public Optional<PowerTypeDto> replacePowerType(Long id, PowerTypeDto powerTypeDto) {
        if (!powerTypeRepository.existsById(id)) {
            return Optional.empty();
        } else {
            powerTypeDto.setId(id);
            PowerType powerTypeToUpdate = powerTypeDtoMapper.map(powerTypeDto);
            PowerType updatedEntity = powerTypeRepository.save(powerTypeToUpdate);
            return Optional.of(powerTypeDtoMapper.map(updatedEntity));
        }
    }
}
