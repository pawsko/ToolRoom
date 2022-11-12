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

    public List<LocationDtoResponse> getAllLocations() {
        return StreamSupport.stream(locationRepository.findAll().spliterator(), false)
                .map(locationDtoMapper::map)
                .collect(Collectors.toList());
    }
    public Optional<LocationDtoResponse> getLocationById (Long id) {
        return locationRepository.findById(id)
                .map(locationDtoMapper::map);
    }

    public LocationDtoResponse saveLocation(LocationDtoRequest locationDtoRequest) {
        Location location = locationDtoMapper.map(locationDtoRequest);
        Location savedLocation = locationRepository.save(location);
        return locationDtoMapper.map(savedLocation);
    }

    public Optional<LocationDtoResponse> replaceLocation(Long id, LocationDtoRequest locationDtoRequest) {
        if (!locationRepository.existsById(id)) {
            return Optional.empty();
        } else {
            Location locationToUpdate = locationDtoMapper.map(locationDtoRequest);
            locationToUpdate.setId(id);
            Location updatedEntity = locationRepository.save(locationToUpdate);
            return Optional.of(locationDtoMapper.map(updatedEntity));
        }
    }
}
