package pl.pawsko.toolroom.manufacturer;

import org.springframework.stereotype.Service;

@Service
class ManufacturerDtoMapper {
    ManufacturerDtoResponse map(Manufacturer manufacturer) {
        ManufacturerDtoResponse dto = new ManufacturerDtoResponse();
        dto.setId(manufacturer.getId());
        dto.setManufacturerName(manufacturer.getManufacturerName());
        return dto;
    }

    Manufacturer map(ManufacturerDtoRequest manufacturerDtoRequest) {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerName(manufacturerDtoRequest.getManufacturerName());
        return manufacturer;
    }
}
