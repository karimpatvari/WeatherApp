package Apps.Weather.Json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Clouds {
    @JsonProperty("all")
    private int all;

}
