package com.booking.ticketing.models;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@SuperBuilder
public class Ticket {
    private String ticketName;
    private BigDecimal price;
    private String description;
    private AtomicInteger capacity;
    private String eventName;
    private String slotName;

    public boolean acquireTicket(){
        return acquireTickets(1);
    }

    public boolean acquireTickets(int count) {
        return capacity.getAndUpdate(currentCapacity -> {
            if (currentCapacity >= count) {
                return currentCapacity - count;
            } else {
                return currentCapacity;
            }
        }) == count;
    }
}
