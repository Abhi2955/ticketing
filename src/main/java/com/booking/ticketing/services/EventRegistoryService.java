package com.booking.ticketing.services;

import com.booking.ticketing.enums.EventType;
import com.booking.ticketing.models.Event;
import com.booking.ticketing.models.Slot;
import com.booking.ticketing.models.Ticket;

import java.util.List;
import java.util.Optional;

public interface EventRegistoryService {
    Event registerEvent(String eventName, EventType type, String address, String startDate, String endDate);
    Slot addSlot(String eventName, String slotName, String startFrom, int hour, int minutes, int slotMinutes);
    Slot addTicketsToSlot(Ticket ticket);
    Optional<Event> findEventByName(String eventName);
    Optional<Slot> findByEventNameAndSlotName(String eventName, String slotName);
    Optional<Ticket> findByEventNameAndSlotNameAndTicketName(String eventName, String slotName, String ticketName);
    List<Event> findByDateRange(String startDate, String endDate);
}
