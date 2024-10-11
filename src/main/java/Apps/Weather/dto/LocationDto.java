package Apps.Weather.dto;

import Apps.Weather.models.User;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
public class LocationDto{

    private Integer id;
    private String name;
    private Integer userId;
    private double latitude;
    private double longitude;

}