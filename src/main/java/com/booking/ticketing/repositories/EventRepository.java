package com.booking.ticketing.repositories;

import com.booking.ticketing.exceptions.DuplicateNameException;
import com.booking.ticketing.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class EventRepository {

    @Autowired
    private BaseRepository<Event> baseRepository;

    private Map<String, String> nameBasedIndex = new HashMap<>();
    private TreeMap<LocalDate, Set<String>> eventDateIndex= new TreeMap<>();

    public Event save(Event event){
        if(event.getId() == null && nameBasedIndex.containsKey(event.getEventName())){
           throw new DuplicateNameException(String.format("Event with Name : %s already Exsits", event.getEventName()));
        }
        Event upserted =  baseRepository.save(event, Event.class);
        performIndex(upserted);
        return upserted;
    }

    private void performIndex(Event event) {
        performNameBasedIndex(event.getEventName(), event.getId());
        performDateBasedIndex(event.getEventStarts(), event.getId());
        performDateBasedIndex(event.getEventEnds(), event.getId());
    }

    private void performDateBasedIndex(LocalDate eventDate, String id) {
        if(eventDateIndex.containsKey(eventDate)){
            Set<String> eventIds = eventDateIndex.get(eventDate);
            eventIds.add(id);
        } else {
            Set<String> eventIds = new HashSet<>();
            eventIds.add(id);
            eventDateIndex.put(eventDate, eventIds);
        }
    }

    private void performNameBasedIndex(String eventName, String id) {
        nameBasedIndex.put(eventName, id);
    }

    public Optional<Event> findByName(String eventName){
        if(nameBasedIndex.containsKey(eventName)){
            return baseRepository.findById(nameBasedIndex.get(eventName), Event.class);
        }
        return Optional.empty();
    }

    public List<Event> findByDateRange(LocalDate startDate, LocalDate endDate) {
        Set<String> eventIds = new HashSet<>();
        eventDateIndex.subMap(startDate, true, endDate, true).values().forEach(eventIds::addAll);
        return baseRepository.findByIds(eventIds, Event.class);
    }
}
