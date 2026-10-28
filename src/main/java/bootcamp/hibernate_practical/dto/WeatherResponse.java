package bootcamp.hibernate_practical.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeatherResponse {
    private WeatherCurrentDTO current;
}
