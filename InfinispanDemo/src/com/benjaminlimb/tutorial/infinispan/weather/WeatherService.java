package com.benjaminlimb.tutorial.infinispan.weather;

public interface WeatherService {
   LocationWeather getWeatherForLocation(String location);
}
