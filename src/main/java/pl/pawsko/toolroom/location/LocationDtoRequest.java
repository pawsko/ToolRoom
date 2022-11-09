package pl.pawsko.toolroom.location;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LocationDtoRequest {
    @NotNull
    @Size(min = 3, message = "Location name must contain minimum {min} signs")
    private String locationName;
}
