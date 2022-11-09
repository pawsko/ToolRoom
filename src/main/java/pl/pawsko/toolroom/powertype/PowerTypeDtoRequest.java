package pl.pawsko.toolroom.powertype;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PowerTypeDtoRequest {
    @NotNull
    @Size(min = 3, message = "Power type name must contain minimum {min} signs")
    private String powerTypeName;
}
