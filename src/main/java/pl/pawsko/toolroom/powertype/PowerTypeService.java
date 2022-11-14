package pl.pawsko.toolroom.powertype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
class PowerTypeService {
    private final PowerTypeRepository powerTypeRepository;
    private final PowerTypeDtoMapper powerTypeDtoMapper;

    List<PowerTypeDtoResponse> getAllPowerTypes() {
        return StreamSupport.stream(powerTypeRepository.findAll().spliterator(), false)
                .map(powerTypeDtoMapper::map)
                .collect(Collectors.toList());
    }

    Optional<PowerTypeDtoResponse> findById(long id) {
        return powerTypeRepository.findById(id)
                .map(powerTypeDtoMapper::map);
    }

    PowerTypeDtoResponse savePowerType(PowerTypeDtoRequest powerTypeDtoRequest) {
        PowerType powerType = powerTypeDtoMapper.map(powerTypeDtoRequest);
        PowerType savedPowerType = powerTypeRepository.save(powerType);
        return powerTypeDtoMapper.map(savedPowerType);
    }

    Optional<PowerTypeDtoResponse> replacePowerType(Long id, PowerTypeDtoRequest powerTypeDtoRequest) {
        if (!powerTypeRepository.existsById(id)) {
            return Optional.empty();
        } else {
            PowerType powerTypeToUpdate = powerTypeDtoMapper.map(powerTypeDtoRequest);
            powerTypeToUpdate.setId(id);
            PowerType updatedEntity = powerTypeRepository.save(powerTypeToUpdate);
            return Optional.of(powerTypeDtoMapper.map(updatedEntity));
        }
    }
}
