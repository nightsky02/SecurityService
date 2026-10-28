package bootcamp.hibernate_practical.service;

import bootcamp.hibernate_practical.dto.WeatherResponse;
import bootcamp.hibernate_practical.exceptions.WeatherServiceError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;


@Service
public class WeatherService {
    private RestClient client;

    @Value("${weather_api_key:null}")
    private String weatherAPIKey;

    private final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final String CURRENT_WEATHER_DATA = "https://api.weatherapi.com/v1/current.json?key=%s&q=%s";

    public WeatherService(@Value("${weather_api_key:null}") String apiKey){
        if(apiKey == null || apiKey.isBlank()){
            throw new WeatherServiceError("Invalid API key: " + this.weatherAPIKey);
        }

        this.weatherAPIKey = apiKey;
        this.initializeRestClient();
    }

    public WeatherResponse getWeatherForRiga(){

        String formattedUrl = String.format(this.CURRENT_WEATHER_DATA, this.weatherAPIKey, "Riga");
        logger.info("Sending request to: {}", formattedUrl);

        WeatherResponse response;

        try {
            response = this.client
                    .get()
                    .uri(formattedUrl)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        throw new WeatherServiceError("Client HTTP error: " + res.getStatusCode());
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        throw new WeatherServiceError("Server HTTP error: " + res.getStatusCode());
                    })
                    .body(WeatherResponse.class);
        } catch(RestClientException ex){
            throw new WeatherServiceError("Cannot send request: " + ex.getMessage());
        }
        return response;
    }

    private void initializeRestClient(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(3000); // 3 seconds
        factory.setConnectTimeout(5000); // 5 seconds

        this.client = RestClient.builder()
                .requestFactory(factory)
                .build();
    }
}
