package pl.pawsko.toolroom.status;

import org.springframework.stereotype.Service;

@Service
public class StatusDtoMapper {
    StatusDto map(Status status) {
        StatusDto dto = new StatusDto();
        dto.setId(status.getId());
        dto.setStatusName(status.getStatusName());
        return dto;
    }

    Status map(StatusDto statusDto) {
        Status status = new Status();
        status.setId(statusDto.getId());
        status.setStatusName(statusDto.getStatusName());
        return status;
    }
}
