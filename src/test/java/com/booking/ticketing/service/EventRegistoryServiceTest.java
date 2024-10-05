package com.booking.ticketing.service;

import com.booking.ticketing.enums.EventType;
import com.booking.ticketing.exceptions.DuplicateNameException;
import com.booking.ticketing.exceptions.NotFoundException;
import com.booking.ticketing.models.Event;
import com.booking.ticketing.models.Slot;
import com.booking.ticketing.models.Ticket;
import com.booking.ticketing.services.EventRegistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class EventRegistoryServiceTest {
    @Autowired
    private EventRegistoryService eventRegistoryService;

    @Test
    void createAEvent_HappyTestCase(){
        String eventName = "Music Concert (Bollywood)";
        String address = "PVR, World Trade Park, Jaipur";
        Event event = eventRegistoryService.registerEvent(eventName, EventType.CONCERT, address, "01-01-2025", "15-01-2025");
        assertEquals(eventName, event.getEventName());
    }

    @Test
    void addSlotInAEvent_HappyTestCase(){
        String eventName = "Music Concert - Gippy";
        String slotName = "Friday Night";
        String address = "PVR, World Trade Park, Jaipur";
        Event event = eventRegistoryService.registerEvent(eventName, EventType.CONCERT, address, "01-01-2025", "15-01-2025");
        Slot slot = eventRegistoryService.addSlot(eventName, slotName, "01-01-2025", 9, 30, 45);
        assertEquals(eventName, event.getEventName());
        assertEquals(slotName, slot.getSlotName());
    }

    @Test
    void addSlotInAEvent_EventNotFound(){
        String eventName = "Music Concert - YoYo";
        String slotName = "Friday Night";
        String address = "PVR, World Trade Park, Jaipur";
        Event event = eventRegistoryService.registerEvent(eventName, EventType.CONCERT, address, "01-01-2025", "15-01-2025");
        assertThrows(NotFoundException.class, () -> {
            eventRegistoryService.addSlot(eventName+"_modified", slotName, "01-01-2025", 9, 30, 45);
        });
    }

    @Test
    void addTicketsIntoSlots_HappyTestCase(){
        String eventName = "Yoyo Nights";
        String slotName = "Friday Night";
        String address = "PVR, World Trade Park, Jaipur";
        eventRegistoryService.registerEvent(eventName, EventType.CONCERT, address, "01-01-2025", "15-01-2025");
        eventRegistoryService.addSlot(eventName, slotName, "01-01-2025", 9, 30, 45);
        Ticket ticket1 = Ticket.builder()
                .ticketName("Platinum")
                .capacity(new AtomicInteger(100))
                .price(BigDecimal.valueOf(500))
                .slotName(slotName)
                .eventName(eventName)
                .build();

        Ticket ticket2 = Ticket.builder()
                .ticketName("Silver")
                .capacity(new AtomicInteger(500))
                .price(BigDecimal.valueOf(350))
                .slotName(slotName)
                .eventName(eventName)
                .build();
        eventRegistoryService.addTicketsToSlot(ticket1);
        Slot slot = eventRegistoryService.addTicketsToSlot(ticket2);

        assertEquals(2, slot.getTickets().size());
        assertEquals(500, slot.getTicketByName("Platinum").get().getPrice().intValue());
    }

    @Test
    void searchByDateRange_HappyTestCase(){
        String eventName = "Music Concert - Gurdas Man";
        String startDate1 = "01-01-2022";
        String startDate2 = "01-02-2022";
        String startDate3 = "01-03-2022";
        String startDate4 = "15-03-2022";
        String endDate1 = "31-01-2022";
        String endDate2 = "28-02-2022";
        String endDate3 = "31-03-2022";
        eventRegistoryService.registerEvent(eventName, EventType.CONCERT, "", startDate1, endDate1);
        eventRegistoryService.registerEvent(eventName+"_1", EventType.CONCERT, "", startDate2, endDate2);
        eventRegistoryService.registerEvent(eventName+"_2", EventType.CONCERT, "", startDate3, endDate3);
        eventRegistoryService.registerEvent(eventName+"_3", EventType.CONCERT, "", startDate4, endDate3);
        eventRegistoryService.registerEvent(eventName+"_4", EventType.CONCERT, "", endDate3, "01-04-2025");

        List<Event> events = eventRegistoryService.findByDateRange("15-01-2022", "15-03-2022");

        assertEquals(4, events.size());
    }

    @Test
    void searchByDateRange_InsertingEventWithEsitingName(){
        String eventName = "Music World";
        String startDate = "01-01-2025";
        String endDate = "31-01-2025";
        eventRegistoryService.registerEvent(eventName, EventType.CONCERT, "", startDate, endDate);
        assertThrows(DuplicateNameException.class, () -> {
            eventRegistoryService.registerEvent(eventName, EventType.CONCERT, "", startDate, endDate);
        });
    }
}
