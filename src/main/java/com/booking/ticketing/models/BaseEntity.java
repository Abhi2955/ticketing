package com.booking.ticketing.models;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@SuperBuilder
public class BaseEntity implements Identifable {
    private String id;
    private Instant createdAt;
    private Instant updatedAt;

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
    @Override
    public void setCreatedAt(Instant timestamp){
        this.createdAt = timestamp;
    }

    @Override
    public Instant getCreatedAt(){
        return this.createdAt;
    }

    @Override
    public void setUpdatedAt(Instant timestamp){
        this.updatedAt = timestamp;
    }
    @Override
    public Instant getUpdatedAt(){
        return this.updatedAt;
    }
}
