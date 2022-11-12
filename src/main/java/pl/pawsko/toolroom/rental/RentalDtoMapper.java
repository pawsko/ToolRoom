package pl.pawsko.toolroom.rental;

import org.springframework.stereotype.Service;
import pl.pawsko.toolroom.tool.Tool;
import pl.pawsko.toolroom.user.User;

@Service
public class RentalDtoMapper {
    RentalDtoResponse map(Rental rental) {
        RentalDtoResponse dto = new RentalDtoResponse();
        dto.setId(rental.getId());
        dto.setRented(rental.getRented());
        dto.setReturned(rental.getReturned());
        dto.setNotices(rental.getNotices());
        dto.setUser(rental.getUser());
        dto.setTool(rental.getTool());
        return dto;
    }

    public Rental map(RentalDtoRequest rentalDtoRequest) {
        Rental rental = new Rental();
        rental.setRented(rentalDtoRequest.getRented());
        rental.setReturned(rentalDtoRequest.getReturned());
        rental.setNotices(rentalDtoRequest.getNotices());
        User user = new User();
        user.setId(rentalDtoRequest.getUserId());
        rental.setUser(user);
        Tool tool = new Tool();
        tool.setId(rentalDtoRequest.getToolId());
        rental.setTool(tool);
        return rental;
    }
}
