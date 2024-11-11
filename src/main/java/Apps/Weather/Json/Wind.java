package Apps.Weather.Json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Wind {
    @JsonProperty("speed")
    private double speed;
    @JsonProperty("deg")
    private int deg;
    @JsonProperty("gust")
    private double gust;

}
