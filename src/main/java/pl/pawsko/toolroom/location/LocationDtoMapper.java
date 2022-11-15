package pl.pawsko.toolroom.location;

import org.springframework.stereotype.Service;

@Service
class LocationDtoMapper {
    LocationDtoResponse map(Location location) {
        LocationDtoResponse dto = new LocationDtoResponse();
        dto.setId(location.getId());
        dto.setLocationName(location.getLocationName());
        return dto;
    }

    Location map(LocationDtoRequest locationDtoRequest) {
        Location location = new Location();
        location.setLocationName(locationDtoRequest.getLocationName());
        return location;
    }
}
