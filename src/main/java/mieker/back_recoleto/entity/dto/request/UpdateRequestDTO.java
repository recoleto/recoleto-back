package mieker.back_recoleto.entity.dto.request;

import lombok.Data;
import mieker.back_recoleto.entity.Enum.RequestStatus;

@Data
public class UpdateRequestDTO {
    private RequestStatus status;
}
