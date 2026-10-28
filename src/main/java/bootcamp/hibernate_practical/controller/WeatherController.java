package bootcamp.hibernate_practical.controller;

import bootcamp.hibernate_practical.exceptions.WeatherServiceError;
import bootcamp.hibernate_practical.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Weather API")
@RestController
public class WeatherController {

    private final WeatherService service;

    public WeatherController(WeatherService service){this.service = service;}

    @Operation(summary = "Receives weather data for Riga city")
    @ApiResponse(responseCode = "200", description = "The weather data has been received successfully")
    @ApiResponse(responseCode = "502", description = "Weather API error occurred")
    @GetMapping("/weather/current")
    public ResponseEntity<?> getCurrentWeather(){
        try{
            return ResponseEntity.ok(service.getWeatherForRiga().getCurrent());
        } catch(WeatherServiceError ex){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ex.getMessage());
        }
    }
}
