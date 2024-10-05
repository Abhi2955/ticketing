package com.booking.ticketing.models;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.*;

@Data
@SuperBuilder
public class Slot extends BaseEntity{
    private String slotName;
    private String eventName;
    private LocalDateTime from;
    private LocalDateTime to;
    private Map<String, Ticket> tickets;

    public synchronized void addTicketToSlot(Ticket ticket){
        if(tickets == null){
            tickets = new HashMap<>();
        }
        tickets.put(ticket.getTicketName(), ticket);
    }

    public Optional<Ticket> getTicketByName(String name){
        if(tickets.containsKey(name)){
            return Optional.ofNullable(tickets.get(name));
        }
        return Optional.empty();
    }

    public Collection<Ticket> getTickets(){
        if(tickets == null){
            return List.of();
        }
        return tickets.values();
    }
}
