package pl.pawsko.toolroom.user;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserDtoMapperTest {
   
    @Test
    void testMapFromUserToUserDtoResponse() {
        UserDtoMapper userDtoMapper = new UserDtoMapper();

        User user = new User(1L, "Pawel", "Skora", "123456789",
                "pawel@email.org", 10, LocalDateTime.now(), null);
        UserDtoResponse mapped = userDtoMapper.map(user);
        assertEquals(mapped.getId(), user.getId());
        assertEquals(mapped.getFirstName(), user.getFirstName());
        assertEquals(mapped.getLastName(), user.getLastName());
        assertEquals(mapped.getEmail(), user.getEmail());
        assertEquals(mapped.getPhoneNumber(), user.getPhoneNumber());
        assertNotEquals(mapped.getFirstName(), "anyUser");
        assertNotEquals(mapped.getId(), 4321L);
    }

    @Test
    void testMapFromUserDtoRequestToUser() {
        UserDtoMapper userDtoMapper = new UserDtoMapper();

        UserDtoRequest userDtoRequest = new UserDtoRequest("Pawel", "Skora", "123456789",
                "pawel@email.org", 10);
        User mapped = userDtoMapper.map(userDtoRequest);
        assertEquals(mapped.getFirstName(), userDtoRequest.getFirstName());
        assertEquals(mapped.getLastName(), userDtoRequest.getLastName());
        assertEquals(mapped.getEmail(), userDtoRequest.getEmail());
        assertEquals(mapped.getPhoneNumber(), userDtoRequest.getPhoneNumber());
        assertEquals(mapped.getRating(), userDtoRequest.getRating());
        assertNotEquals(mapped.getFirstName(), "anyUser");
    }
}