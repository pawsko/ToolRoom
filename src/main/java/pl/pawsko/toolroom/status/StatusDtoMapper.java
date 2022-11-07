package pl.pawsko.toolroom.status;

import org.springframework.stereotype.Service;

@Service
public class StatusDtoMapper {
    StatusDtoResponse map(Status status) {
        StatusDtoResponse dto = new StatusDtoResponse();
        dto.setId(status.getId());
        dto.setStatusName(status.getStatusName());
        return dto;
    }

    Status map(StatusDtoRequest statusDtoRequest) {
        Status status = new Status();
        status.setStatusName(statusDtoRequest.getStatusName());
        return status;
    }
}
