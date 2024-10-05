package com.booking.ticketing.models;

import java.time.Instant;

public interface Identifable {
    void setId(String id);
    String getId();
    void setCreatedAt(Instant timestamp);
    Instant getCreatedAt();
    void setUpdatedAt(Instant timestamp);
    Instant getUpdatedAt();
}
