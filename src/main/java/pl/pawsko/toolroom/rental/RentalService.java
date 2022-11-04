package pl.pawsko.toolroom.rental;

import org.springframework.stereotype.Service;
import pl.pawsko.toolroom.category.CategoryDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RentalService {
    private final RentalRepository rentalRepository;
    private final RentalDtoMapper rentalDtoMapper;

    public RentalService(RentalRepository rentalRepository, RentalDtoMapper rentalDtoMapper) {
        this.rentalRepository = rentalRepository;
        this.rentalDtoMapper = rentalDtoMapper;
    }

    public List<RentalDto> getAllRentals() {
        return StreamSupport.stream(rentalRepository.findAll().spliterator(), false)
                .map(rentalDtoMapper::map)
                .collect(Collectors.toList());
    }

    public Optional<RentalDto> getRentalById(Long id) {
        return rentalRepository.findById(id)
                .map(rentalDtoMapper::map);
    }

    public RentalDto saveRental(RentalDto rentalDto) {
        Rental rental = rentalDtoMapper.map(rentalDto);
        Rental savedRental = rentalRepository.save(rental);
        return rentalDtoMapper.map(savedRental);
    }

    public Optional<RentalDto> replaceRental(Long id, RentalDto rentalDto) {
        if (!rentalRepository.existsById(id)) {
            return Optional.empty();
        } else {
            rentalDto.setId(id);
            Rental rentalToUpdate = rentalDtoMapper.map(rentalDto);
            Rental updatedEntity = rentalRepository.save(rentalToUpdate);
            return Optional.of(rentalDtoMapper.map(updatedEntity));
        }
    }
}
