package pl.pawsko.toolroom.user;

import org.springframework.stereotype.Service;

@Service
public class UserDtoMapper {
    public UserDto map(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRating(user.getRating());
        dto.setCreated(user.getCreated());
        dto.setLastUpdate(user.getLastUpdate());
        return dto;
    }

    public User map(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setRating(userDto.getRating());
        return user;
    }
}
