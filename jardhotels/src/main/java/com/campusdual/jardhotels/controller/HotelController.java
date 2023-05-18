package com.campusdual.jardhotels.controller;

import com.campusdual.jardhotels.api.IHotelService;
import com.campusdual.jardhotels.exceptions.HotelNotFound;
import com.campusdual.jardhotels.model.dto.HotelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private IHotelService hotelService;

    @PostMapping(value = "/add")
    public int addHotel(@RequestBody HotelDTO hotelDTO) {
        return hotelService.insertHotel(hotelDTO);
    }

    @DeleteMapping(value = "/delete")
    public int deleteHotel(@RequestBody HotelDTO hotelDTO) {
        try {
            hotelService.queryHotel(hotelDTO);
        } catch (Exception e) {
            throw new HotelNotFound("Hotel not found");
        }
        return hotelService.deleteHotel(hotelDTO);
    }

    @GetMapping(value = "/getAll")
    public List<HotelDTO> getAllHotels() {
        return hotelService.queryAll();
    }

}
