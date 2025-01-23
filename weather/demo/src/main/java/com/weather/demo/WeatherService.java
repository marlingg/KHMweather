package com.weather.demo;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    private static final String API_KEY = "e9a269c5421ee820bf9288b23374e021"; // Ваш API-ключ
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q={city}&appid={apiKey}";

    public String getWeather(String city) {
      
            RestTemplate restTemplate = new RestTemplate();
            String url = WEATHER_URL.replace("{city}", city).replace("{apiKey}", API_KEY);
            return restTemplate.getForObject(url, String.class);
    }
}








