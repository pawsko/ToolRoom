package pl.pawsko.toolroom.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
class UserDtoRequest {
    @NotNull
    @Size(min = 3, message = "First name must contain minimum {min} signs")
    private String firstName;
    @NotNull
    @Size(min = 3, message = "Last name must contain minimum {min} signs")
    private String lastName;
    @NotNull
    @Size(min = 9, max = 11, message = "Phone number must contain minimum {min} and {max} digits")
    private String phoneNumber;
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    private String email;
    @Schema(minimum = "0", maximum = "10", description = "rating 0-10")
    @Min(value = 0, message = "Rating must be at least {value}")
    @Max(value = 10, message = "Rating must be equal or less then {value}")
    private int rating;
}
