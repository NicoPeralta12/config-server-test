package com.todocodeacademy.hotelsservice.service;

import com.todocodeacademy.hotelsservice.models.Hotel;

import java.util.List;

public interface IHotelService {
    public List<Hotel> getHotelsByCityId(Long city_id);

    public String hola();
}
