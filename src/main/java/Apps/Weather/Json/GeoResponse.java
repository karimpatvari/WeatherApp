package Apps.Weather.Json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class GeoResponse {
    @JsonProperty("name")
    private String name;
    @JsonProperty("local_names")
    private Map<String, String> localNames;
    @JsonProperty("lat")
    private double lat;
    @JsonProperty("lon")
    private double lon;
    @JsonProperty("country")
    private String country;
    @JsonProperty("state")
    private String state;

    public Optional<String> getState() {
        return Optional.ofNullable(state);
    }

    @Override
    public String toString() {

        String stateStr = "";
        if (state != null ) {
            stateStr = ", state=" + state + "\n";
        }

        return "GeoResponse{" +
                "name='" + name + '\'' +
                ", localNames=" + localNames +
                ", lat=" + lat +
                ", lon=" + lon +
                ", country='" + country + '\'' +
                stateStr +
                '}';
    }
}
