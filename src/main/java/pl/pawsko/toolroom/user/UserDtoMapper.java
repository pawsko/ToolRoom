package pl.pawsko.toolroom.user;

import org.springframework.stereotype.Service;

@Service
public class UserDtoMapper {
    public UserDtoResponse map(User user) {
        UserDtoResponse dto = new UserDtoResponse();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        return dto;
    }

    public User map(UserDtoRequest userDtoRequest) {
        User user = new User();
        user.setFirstName(userDtoRequest.getFirstName());
        user.setLastName(userDtoRequest.getLastName());
        user.setEmail(userDtoRequest.getEmail());
        user.setPhoneNumber(userDtoRequest.getPhoneNumber());
        user.setRating(userDtoRequest.getRating());
        return user;
    }
}
