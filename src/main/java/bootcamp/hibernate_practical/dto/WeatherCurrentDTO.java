package bootcamp.hibernate_practical.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WeatherCurrentDTO {
    private double temp_c;
    private int humidity;
    private int cloud;
}
