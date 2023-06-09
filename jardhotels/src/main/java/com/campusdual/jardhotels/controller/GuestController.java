package com.campusdual.jardhotels.controller;

import com.campusdual.jardhotels.api.IGuestService;
import com.campusdual.jardhotels.exceptions.GuestNotFound;
import com.campusdual.jardhotels.exceptions.HotelNotFound;
import com.campusdual.jardhotels.model.dto.GuestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guests")
public class GuestController {

    @Autowired
    private IGuestService guestService;

    @PostMapping(value = "/add")
    public int addGuest(@RequestBody GuestDTO guestDTO) {
        return guestService.insertGuest(guestDTO);
    }


    @PostMapping(value = "/get")
    public GuestDTO getGuest(@RequestBody GuestDTO guestDTO) {
        return guestService.queryGuest(guestDTO);
    }

    @DeleteMapping(value = "/delete")
    public int deleteGuest(@RequestBody GuestDTO guestDTO) {
        try {
            guestService.queryGuest(guestDTO);
        } catch (Exception e) {
            throw new GuestNotFound("Guest not found");
        }
        return guestService.deleteGuest(guestDTO);
    }

    @GetMapping(value = "/getAll")
    public List<GuestDTO> getAllGuests() {
        return guestService.queryAll();
    }

}
