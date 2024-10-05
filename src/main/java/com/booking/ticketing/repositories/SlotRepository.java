package com.booking.ticketing.repositories;

import com.booking.ticketing.models.Slot;
import com.sun.source.tree.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class SlotRepository {
    @Autowired
    private BaseRepository<Slot> baseRepository;

    private Map<String, Map<String, String>> eventAndSlotNameBasedIndex = new HashMap<>();

    public Slot save(Slot slot){
        Slot upserted =  baseRepository.save(slot, Slot.class);
        performIndex(upserted);
        return upserted;
    }

    private void performIndex(Slot upserted) {
        performNameBasedIndex(upserted.getEventName(), upserted.getSlotName(), upserted.getId());
    }

    private void performNameBasedIndex(String eventName, String slotName, String id) {
        if(eventAndSlotNameBasedIndex.containsKey(eventName)){
            Map<String, String> slotNameAndId = eventAndSlotNameBasedIndex.get(eventName);
            slotNameAndId.put(slotName, id);
        } else {
           Map<String, String> slotNameAndId = new HashMap<>();
           slotNameAndId.put(slotName, id);
           eventAndSlotNameBasedIndex.put(eventName, slotNameAndId);
        }
    }

    public Optional<Slot> findByEventNameAndSlotName(String eventName, String slotName){
        if(eventAndSlotNameBasedIndex.containsKey(eventName)){
            Map<String, String> innerIndex = eventAndSlotNameBasedIndex.get(eventName);
            if(innerIndex.containsKey(slotName)){
                return baseRepository.findById(innerIndex.get(slotName), Slot.class);
            }
        }
        return Optional.empty();
    }

    public List<Slot> findByEventName(String eventName){
        if(eventAndSlotNameBasedIndex.containsKey(eventName)){
            Map<String, String> innerIndex = eventAndSlotNameBasedIndex.get(eventName);
            return baseRepository.findByIds(innerIndex.values(), Slot.class);
        }
        return List.of();
    }
}
