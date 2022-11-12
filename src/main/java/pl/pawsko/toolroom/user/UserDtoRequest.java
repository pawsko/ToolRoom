package pl.pawsko.toolroom.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(hidden = true)

public class UserDtoRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private int rating;
}
