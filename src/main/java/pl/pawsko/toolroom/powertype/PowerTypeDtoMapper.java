package pl.pawsko.toolroom.powertype;

import org.springframework.stereotype.Service;

@Service
public class PowerTypeDtoMapper {
    public PowerTypeDto map(PowerType powerType) {
        PowerTypeDto dto = new PowerTypeDto();
        dto.setId(powerType.getId());
        dto.setPowerTypeName(powerType.getPowerTypeName());
        return dto;
    }

    public PowerType map(PowerTypeDto powerTypeDto) {
        PowerType powerType = new PowerType();
        powerType.setId(powerTypeDto.getId());
        powerType.setPowerTypeName(powerTypeDto.getPowerTypeName());
        return powerType;
    }
}
