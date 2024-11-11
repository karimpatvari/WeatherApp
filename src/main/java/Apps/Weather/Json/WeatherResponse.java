package Apps.Weather.Json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class WeatherResponse {

    private Integer locationId;

    @JsonProperty("coord")
    private Coord coord;
    @JsonProperty("weather")
    private List<Weather> weather;
    @JsonProperty("base")
    private String base;
    @JsonProperty("main")
    private Main main;
    @JsonProperty("visibility")
    private int visibility;
    @JsonProperty("wind")
    private Wind wind;
    @JsonProperty("rain")
    private Rain rain;
    @JsonProperty("snow")
    private Snow snow;
    @JsonProperty("clouds")
    private Clouds clouds;
    @JsonProperty("dt")
    private long dt;
    @JsonProperty("sys")
    private Sys sys;
    @JsonProperty("timezone")
    private int timezone;
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("cod")
    private int cod;

    @Override
    public String toString() {

        String rainStr = "";
        if (rain != null){
            rainStr = ", rain=" + rain + "\n";
        }

        String snowStr = "";
        if (snow != null){
            snowStr = ", snow=" + snow + "\n";
        }

        return "WeatherResponse{" +
                "coord=" + coord.toString() + "\n" +
                ", weather=" + weather.toString() + "\n" +
                ", base='" + base.toString() + "\n" +
                ", main=" + main.toString() + "\n" +
                ", visibility=" + visibility + "\n" +
                ", wind=" + wind.toString() + "\n" +
                rainStr +
                snowStr +
                ", clouds=" + clouds.toString() + "\n" +
                ", dt=" + dt + "\n" +
                ", sys=" + sys.toString() + "\n" +
                ", timezone=" + timezone + "\n" +
                ", id=" + id + "\n" +
                ", name='" + name +  "\n" +
                ", cod=" + cod + "\n" +
                '}';
    }

    public Optional<Rain> getRain() {
        return Optional.ofNullable(rain);
    }

    public Optional<Snow> getSnow() {
        return Optional.ofNullable(snow);
    }

}

