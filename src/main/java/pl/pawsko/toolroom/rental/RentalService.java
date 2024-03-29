package pl.pawsko.toolroom.rental;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
class RentalService {
    private final RentalRepository rentalRepository;
    private final RentalDtoMapper rentalDtoMapper;

    List<RentalDtoResponse> getAllRentals() {
        return StreamSupport.stream(rentalRepository.findAll().spliterator(), false)
                .map(rentalDtoMapper::map)
                .collect(Collectors.toList());
    }

    Optional<RentalDtoResponse> getRentalById(Long id) {
        return rentalRepository.findById(id)
                .map(rentalDtoMapper::map);
    }

    RentalDtoResponse saveRental(RentalDtoRequest rentalDtoRequest) {
        Rental rental = rentalDtoMapper.map(rentalDtoRequest);
        Rental savedRental = rentalRepository.save(rental);
        return rentalDtoMapper.map(savedRental);
    }

    Optional<RentalDtoResponse> replaceRental(Long id, RentalDtoRequest rentalDtoRequest) {
        if (!rentalRepository.existsById(id)) {
            return Optional.empty();
        } else {
            Rental rentalToUpdate = rentalDtoMapper.map(rentalDtoRequest);
            rentalToUpdate.setId(id);
            Rental updatedEntity = rentalRepository.save(rentalToUpdate);
            return Optional.of(rentalDtoMapper.map(updatedEntity));
        }
    }
}
