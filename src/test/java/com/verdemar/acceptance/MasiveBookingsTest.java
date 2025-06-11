package com.verdemar.acceptance;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.verdemar.domain.Booking;
import com.verdemar.domain.Client;
import com.verdemar.domain.dto.BookingDto;
import com.verdemar.service.booking.BookingService;
import com.verdemar.service.client.ClientService;

public class MasiveBookingsTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ClientService clientService;

    public void createMassiveBookings() {

        for (int i = 0; i < 1000; i++) {
            Client new_client = new Client(null, "Client test" , "testing", "666-666-666", "@test.com");
            Client client = clientService.createClient(new_client);
            BookingDto new_booking = new BookingDto();
            new_booking.setId((long) i);
            new_booking.setClientId(client.getId());
            new_booking.set
            new_booking.setStartDate(LocalDate.now().plusDays(i));
            new_booking.setEndDate(LocalDate.now().plusDays(i + 1));
            bookingService.createBooking(new_booking);
        }  
    }
}
