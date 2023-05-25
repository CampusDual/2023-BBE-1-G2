package com.campusdual.jardhotelsontimize.ws.core.rest;

import com.campusdual.jardhotelsontimize.api.core.service.IHotelService;
import com.ontimize.jee.server.rest.ORestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotels")
public class HotelRestController extends ORestController<IHotelService>{

    @Autowired
    private IHotelService hotelService;
    @Override
    public IHotelService getService() {
        return this.hotelService;
    }
}
