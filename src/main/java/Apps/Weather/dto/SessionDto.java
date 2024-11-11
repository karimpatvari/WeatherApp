package Apps.Weather.dto;

import Apps.Weather.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionDto {

    private String id;
    private Integer userId;
    private Timestamp expiresAt;
}
