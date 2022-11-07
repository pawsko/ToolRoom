package pl.pawsko.toolroom.powertype;

import org.springframework.stereotype.Service;

@Service
public class PowerTypeDtoMapper {
    public PowerTypeDtoResponse map(PowerType powerType) {
        PowerTypeDtoResponse dto = new PowerTypeDtoResponse();
        dto.setId(powerType.getId());
        dto.setPowerTypeName(powerType.getPowerTypeName());
        return dto;
    }

    public PowerType map(PowerTypeDtoRequest powerTypeDtoRequest) {
        PowerType powerType = new PowerType();
        powerType.setPowerTypeName(powerTypeDtoRequest.getPowerTypeName());
        return powerType;
    }
}
