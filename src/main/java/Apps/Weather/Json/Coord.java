package Apps.Weather.Json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Coord {
    @JsonProperty("lon")
    private double lon;
    @JsonProperty("lat")
    private double lat;

}
