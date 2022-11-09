package pl.pawsko.toolroom.manufacturer;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ManufacturerDtoRequest {
    @NotNull
    @Size(min = 3, message = "Manufacturer name must contain minimum {min} signs")
    private String manufacturerName;
}
