package mieker.back_recoleto.entity.dto;

import lombok.Data;
import mieker.back_recoleto.entity.Enum.RequestStatus;

import java.util.UUID;

@Data
public class UpdateRequestDTO {
    private RequestStatus status;
}
