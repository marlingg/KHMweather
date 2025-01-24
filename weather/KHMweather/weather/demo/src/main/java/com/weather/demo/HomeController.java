package com.weather.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class HomeController {

    @Value("${weather.api.key}") // Ваш API ключ из application.properties
    private String apiKey;

    // Главная страница
    @GetMapping("/qwe")
    public String home() {
        return "start"; // Имя шаблона главной страницы (например, index.html)
    }

    // Страница погоды
    @GetMapping("/")
    public String getWeather(Model model) {
        String cityQ = "хмельницкий"; // Можно заменить на параметр @RequestParam, если город вводится пользователем
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + cityQ + "&appid=" + apiKey + "&units=metric&lang=ru";

        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(apiUrl, String.class);

            // Преобразование ответа в JSON
            org.json.JSONObject jsonResponse = new org.json.JSONObject(response);

            // Основные параметры
            double temp = jsonResponse.getJSONObject("main").getDouble("temp");
            double feelsLike = jsonResponse.getJSONObject("main").getDouble("feels_like");
            int pressure = jsonResponse.getJSONObject("main").getInt("pressure");
            int humidity = jsonResponse.getJSONObject("main").getInt("humidity");

            // Погодные условия
            String description = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            String icon = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("icon");

            // Ветер
            double windSpeed = jsonResponse.getJSONObject("wind").getDouble("speed");
            int windDeg = jsonResponse.getJSONObject("wind").optInt("deg", 0); // Угол ветра, опционально
            double windGust = jsonResponse.getJSONObject("wind").optDouble("gust", 0.0); // Порывы ветра

            // Географические координаты
            double lon = jsonResponse.getJSONObject("coord").getDouble("lon");
            double lat = jsonResponse.getJSONObject("coord").getDouble("lat");

            // Восход и закат
            long sunrise = jsonResponse.getJSONObject("sys").getLong("sunrise");
            long sunset = jsonResponse.getJSONObject("sys").getLong("sunset");

            // Форматирование времени восхода и заката
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String formattedSunrise = timeFormat.format(new Date(sunrise * 1000));
            String formattedSunset = timeFormat.format(new Date(sunset * 1000));

            // Передача данных в модель
            model.addAttribute("city", cityQ);
            model.addAttribute("temperature", temp);
            model.addAttribute("feelsLike", feelsLike);
            model.addAttribute("pressure", pressure);
            model.addAttribute("humidity", humidity);
            model.addAttribute("description", description);
            model.addAttribute("icon", icon);
            model.addAttribute("windSpeed", windSpeed);
            model.addAttribute("windDeg", windDeg);
            model.addAttribute("windGust", windGust);
            model.addAttribute("lon", lon);
            model.addAttribute("lat", lat);
            model.addAttribute("sunrise", formattedSunrise);
            model.addAttribute("sunset", formattedSunset);

        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "Ошибка при получении данных с API: " + e.getMessage());
        }

        return "weather"; // Имя шаблона для страницы погоды (например, weather.html)
    }
}
