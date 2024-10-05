package com.booking.ticketing.services.impl;

import com.booking.ticketing.enums.EventType;
import com.booking.ticketing.exceptions.NotFoundException;
import com.booking.ticketing.models.Event;
import com.booking.ticketing.models.Slot;
import com.booking.ticketing.models.Ticket;
import com.booking.ticketing.repositories.EventRepository;
import com.booking.ticketing.repositories.SlotRepository;
import com.booking.ticketing.services.EventRegistoryService;
import com.booking.ticketing.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class EventRegistoryServiceImpl implements EventRegistoryService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Override
    public Event registerEvent(String eventName, EventType type, String address, String startDate, String endDate) {
        Event event = Event.builder()
                .eventName(eventName)
                .eventType(type)
                .address(address)
                .eventStarts(DateUtils.getLocalDate(startDate))
                .eventEnds(DateUtils.getLocalDate(endDate))
                .build();
        return eventRepository.save(event);
    }

    @Override
    public Slot addSlot(String eventName, String slotName, String startFrom, int hour, int minutes, int slotMinutes) {
        Optional<Event> event = this.findEventByName(eventName);
        if(event.isEmpty()){
            throw new NotFoundException(String.format("Event Not Fount with EventName: {}", eventName));
        }
        LocalDateTime startFromDateTIme = DateUtils.getLocalDateTime(startFrom).withHour(hour).withMinute(minutes);
        LocalDateTime endDateTIme = startFromDateTIme.plus(slotMinutes, ChronoUnit.MINUTES);
        Slot slot = Slot.builder()
                .slotName(slotName)
                .eventName(eventName)
                .from(startFromDateTIme)
                .to(endDateTIme)
                .build();
        return slotRepository.save(slot);
    }

    @Override
    public Slot addTicketsToSlot(Ticket ticket) {
        Optional<Slot> slot = this.findByEventNameAndSlotName(ticket.getEventName(), ticket.getSlotName());
        if(slot.isEmpty()){
            throw new NotFoundException(String.format("Event OR Slot Not Fount with EventName: {} AND SlotName: {}", ticket.getEventName(), ticket.getSlotName()));
        }
        slot.get().addTicketToSlot(ticket);
        return slotRepository.save(slot.get());
    }

    @Override
    public Optional<Event> findEventByName(String eventName) {
        return eventRepository.findByName(eventName);
    }

    @Override
    public Optional<Slot> findByEventNameAndSlotName(String eventName, String slotName){
        return slotRepository.findByEventNameAndSlotName(eventName, slotName);
    }

    @Override
    public Optional<Ticket> findByEventNameAndSlotNameAndTicketName(String eventName, String slotName, String ticketName){
        Optional<Slot> slot =  slotRepository.findByEventNameAndSlotName(eventName, slotName);
        if(slot.isPresent()){
            return slot.get().getTicketByName(ticketName);
        }
        return Optional.empty();
    }

    @Override
    public List<Event> findByDateRange(String startDate, String endDate){
        return eventRepository.findByDateRange(DateUtils.getLocalDate(startDate), DateUtils.getLocalDate(endDate));
    }
}
