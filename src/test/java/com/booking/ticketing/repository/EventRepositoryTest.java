package com.booking.ticketing.repository;

import com.booking.ticketing.enums.EventType;
import com.booking.ticketing.models.Event;
import com.booking.ticketing.repositories.BaseRepository;
import com.booking.ticketing.repositories.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EventRepositoryTest {
    @Autowired
    EventRepository eventRepository;

    @Test
    void insertAndFindByname_Happy(){
        Event event = Event.builder()
                .eventName("event")
                .eventType(EventType.MOVIE)
                .build();
        Event inserted = eventRepository.save(event);
        assertEquals(inserted.getEventName(), event.getEventName());
        Event findByResult = eventRepository.findByName(event.getEventName()).get();
        assertEquals(inserted, findByResult);
    }
}
