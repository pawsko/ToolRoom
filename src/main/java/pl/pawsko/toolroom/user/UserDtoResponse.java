package pl.pawsko.toolroom.user;

import lombok.Data;

@Data
class UserDtoResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
}
