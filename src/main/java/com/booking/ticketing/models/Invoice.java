package com.booking.ticketing.models;

import com.booking.ticketing.enums.InvoiceStatus;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
public class Invoice {
    private String invoiceNumber;
    private String eventName;
    private List<EntryPass> entires;
    private BigDecimal totalAmountPaid;
    private InvoiceStatus status;
    private LocalDateTime orderPlacedOn;
    private String userName;
}
