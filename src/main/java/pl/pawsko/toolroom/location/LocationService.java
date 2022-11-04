package pl.pawsko.toolroom.location;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationDtoMapper locationDtoMapper;

    public LocationService(LocationRepository locationRepository, LocationDtoMapper locationDtoMapper) {
        this.locationRepository = locationRepository;
        this.locationDtoMapper = locationDtoMapper;
    }

    public List<LocationDto> getAllLocations() {
        return StreamSupport.stream(locationRepository.findAll().spliterator(), false)
                .map(locationDtoMapper::map)
                .collect(Collectors.toList());
    }
    public Optional<LocationDto> getLocationById (Long id) {
        return locationRepository.findById(id)
                .map(locationDtoMapper::map);
    }

    public LocationDto saveLocation(LocationDto locationDto) {
        Location location = locationDtoMapper.map(locationDto);
        Location savedLocation = locationRepository.save(location);
        return locationDtoMapper.map(savedLocation);
    }

    public Optional<LocationDto> replaceLocation(Long id, LocationDto locationDto) {
        if (!locationRepository.existsById(id)) {
            return Optional.empty();
        } else {
            locationDto.setId(id);
            Location locationToUpdate = locationDtoMapper.map(locationDto);
            Location updatedEntity = locationRepository.save(locationToUpdate);
            return Optional.of(locationDtoMapper.map(updatedEntity));
        }
    }
}
