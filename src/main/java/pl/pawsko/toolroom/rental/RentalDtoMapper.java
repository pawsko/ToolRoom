package pl.pawsko.toolroom.rental;

import org.springframework.stereotype.Service;
import pl.pawsko.toolroom.tool.Tool;
import pl.pawsko.toolroom.tool.ToolRepository;
import pl.pawsko.toolroom.user.User;
import pl.pawsko.toolroom.user.UserRepository;

import java.util.Optional;

@Service
public class RentalDtoMapper {

    public final UserRepository userRepository;
    public final ToolRepository toolRepository;

    public RentalDtoMapper(UserRepository userRepository, ToolRepository toolRepository) {
        this.userRepository = userRepository;
        this.toolRepository = toolRepository;
    }

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
        Optional<User> user = userRepository.findById(rentalDtoRequest.getUserId());
        user.ifPresent(rental::setUser);
        Optional<Tool> tool = toolRepository.findById(rentalDtoRequest.getToolId());
        tool.ifPresent(rental::setTool);
        return rental;
    }
}
