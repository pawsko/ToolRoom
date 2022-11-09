package pl.pawsko.toolroom.status;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class StatusDtoRequest {
    @NotNull
    @Size(min = 3, message = "Status name must contain minimum {min} signs")
    private String statusName;
}
