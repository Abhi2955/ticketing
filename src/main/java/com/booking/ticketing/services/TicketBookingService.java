package com.booking.ticketing.services;

import com.booking.ticketing.models.Event;
import com.booking.ticketing.models.Invoice;

import java.util.List;

public interface TicketBookingService {
    List<Event> getEvents(String startDate, String endDate);
    Event getEvent(String eventName);
    Invoice checkout(String userName, String eventName, String slotName, String ticketName, int count);
    List<Invoice> getOrders(String userName);
}
