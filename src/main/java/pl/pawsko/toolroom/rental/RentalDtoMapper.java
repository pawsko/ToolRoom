package pl.pawsko.toolroom.rental;

import org.springframework.stereotype.Service;

@Service
public class RentalDtoMapper {
    RentalDto map(Rental rental) {
        RentalDto dto = new RentalDto();
        dto.setId(rental.getId());
        dto.setRented(rental.getRented());
        dto.setReturned(rental.getReturned());
        dto.setNotices(rental.getNotices());
        dto.setUser(rental.getUser());
        dto.setTool(rental.getTool());
        return dto;
    }

    public Rental map(RentalDto rentalDto) {
        Rental rental = new Rental();
        rental.setId(rentalDto.getId());
        rental.setRented(rentalDto.getRented());
        rental.setReturned(rentalDto.getReturned());
        rental.setNotices(rentalDto.getNotices());
        rental.setUser(rentalDto.getUser());
        rental.setTool(rentalDto.getTool());
        return rental;
    }
}
