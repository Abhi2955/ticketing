package com.booking.ticketing.models;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
public class EntryPass extends BaseEntity{
    private String name;
    private String email;
    private String mobile;
    private String ticketName;
    private String uniquePassId;
    private BigDecimal amount;
}
