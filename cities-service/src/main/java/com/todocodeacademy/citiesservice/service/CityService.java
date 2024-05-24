package com.todocodeacademy.citiesservice.service;

import com.todocodeacademy.citiesservice.dto.CityDTO;
import com.todocodeacademy.citiesservice.models.City;
import com.todocodeacademy.citiesservice.repositories.IHotelsAPI;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityService implements ICityService {

    @Autowired
    IHotelsAPI hotelsAPI;

    public List<City> cities = new ArrayList<>();

    @Override
    @CircuitBreaker(name = "hotels-service", fallbackMethod = "fallbackGetCitiesHotel")
    @Retry(name = "hotels-service")
    public CityDTO getCitiesHotels(String name, String country) {
        City city = this.findCity(name, country);
        if (city == null) {
            return fallbackGetCitiesHotel(name, country, new Exception("City not found"));
        }

        CityDTO cityDTO = new CityDTO();
        cityDTO.setCity_id(city.getCity_id());
        cityDTO.setName(city.getName());
        cityDTO.setCountry(city.getCountry());
        cityDTO.setContinent(city.getContinent());
        cityDTO.setState(city.getState());
        cityDTO.setHotelList(hotelsAPI.getHotelsByCityId(city.getCity_id()));

        return cityDTO;
    }

    public CityDTO fallbackGetCitiesHotel(String name, String country, Throwable throwable) {
        return new CityDTO(99999L, "fallido", "fallido", "fallido", "fallido", null);
    }

    public City findCity(String name, String country) {
        this.loadCities();
        for (City c : cities) {
            if (c.getName().equals(name) && c.getCountry().equals(country)) {
                return c;
            }
        }
        return null;
    }

    public void loadCities() {
        if (cities.isEmpty()) {  // Solo cargar las ciudades una vez
            cities.add(new City(1L, "Buenos Aires", "South America", "Argentina", "Buenos Aires"));
            cities.add(new City(2L, "Oberá", "South America", "Argentina", "Misiones"));
            cities.add(new City(3L, "Mexico City", "North America", "Mexico", "Mexico City"));
            cities.add(new City(4L, "Guadalajara", "North America", "Mexico", "Jalisco"));
            cities.add(new City(5L, "Bogotá", "South America", "Colombia", "Cundinamarca"));
            cities.add(new City(6L, "Medellín", "South America", "Colombia", "Antioquia"));
            cities.add(new City(7L, "Santiago", "South America", "Chile", "Santiago Metropolitan"));
            cities.add(new City(8L, "Valparaíso", "South America", "Chile", "Valparaíso"));
            cities.add(new City(9L, "Asunción", "South America", "Paraguay", "Asunción"));
            cities.add(new City(10L, "Montevideo", "South America", "Uruguay", "Montevideo"));
            cities.add(new City(11L, "Madrid", "Europe", "Spain", "Community of Madrid"));
            cities.add(new City(12L, "Barcelona", "Europe", "Spain", "Catalonia"));
            cities.add(new City(13L, "Seville", "Europe", "Spain", "Andalucia"));
            cities.add(new City(14L, "Monterrey", "North America", "Mexico", "Nuevo León"));
            cities.add(new City(15L, "Valencia", "Europe", "Spain", "Valencian Community"));
        }
    }
}
