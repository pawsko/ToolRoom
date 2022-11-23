package pl.pawsko.toolroom.user;

import org.springframework.stereotype.Service;
import pl.pawsko.toolroom.user.nosql.UserNoSql;

@Service
class UserDtoMapper {
    UserDtoResponse map(User user) {
        UserDtoResponse dto = new UserDtoResponse();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        return dto;
    }

    User map(UserDtoRequest userDtoRequest) {
        User user = new User();
        user.setFirstName(userDtoRequest.getFirstName());
        user.setLastName(userDtoRequest.getLastName());
        user.setEmail(userDtoRequest.getEmail());
        user.setPhoneNumber(userDtoRequest.getPhoneNumber());
        user.setRating(userDtoRequest.getRating());
        return user;
    }

    UserDtoResponse mapFromNoSql(UserNoSql userNoSql) {
        UserDtoResponse dto = new UserDtoResponse();
        dto.setId(userNoSql.getId());
        dto.setFirstName(userNoSql.getFirstName());
        dto.setLastName(userNoSql.getLastName());
        dto.setEmail(userNoSql.getEmail());
        dto.setPhoneNumber(userNoSql.getPhoneNumber());
        return dto;
    }
}
