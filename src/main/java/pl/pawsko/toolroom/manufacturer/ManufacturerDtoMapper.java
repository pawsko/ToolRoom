package pl.pawsko.toolroom.manufacturer;

import org.springframework.stereotype.Service;

@Service
public class ManufacturerDtoMapper {
    ManufacturerDto map(Manufacturer manufacturer) {
        ManufacturerDto dto = new ManufacturerDto();
        dto.setId(manufacturer.getId());
        dto.setManufacturerName(manufacturer.getManufacturerName());
        return dto;
    }

    Manufacturer map(ManufacturerDto manufacturerDto) {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(manufacturerDto.getId());
        manufacturer.setManufacturerName(manufacturerDto.getManufacturerName());
        return manufacturer;
    }
}
