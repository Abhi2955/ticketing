package com.booking.ticketing.models;

import com.booking.ticketing.enums.EventType;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
public class Event extends BaseEntity{
    private String eventName;
    private EventType eventType;
    private String address;
    private String email;
    private LocalDate eventStarts;
    private LocalDate eventEnds;
}
