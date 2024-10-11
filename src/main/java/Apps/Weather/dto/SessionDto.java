package Apps.Weather.dto;

import Apps.Weather.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
public class SessionDto {

    private Integer id;
    private Integer userId;
    private Timestamp sessionTime;
}
