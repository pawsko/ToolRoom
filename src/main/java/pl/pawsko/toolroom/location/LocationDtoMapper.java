package pl.pawsko.toolroom.location;

import org.springframework.stereotype.Service;

@Service
public class LocationDtoMapper {
    LocationDto map(Location location) {
        LocationDto dto = new LocationDto();
        dto.setId(location.getId());
        dto.setLocationName(location.getLocationName());
        return dto;
    }

    Location map(LocationDto locationDto) {
        Location location = new Location();
        location.setId(locationDto.getId());
        location.setLocationName(locationDto.getLocationName());
        return location;
    }
}
