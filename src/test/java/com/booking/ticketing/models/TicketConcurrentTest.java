package com.booking.ticketing.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TicketConcurrentTest {

    @Test
    void concurrentTicketBooking() throws ExecutionException, InterruptedException {
        executeConcurrentTicketBooking(50, 50);
        executeConcurrentTicketBooking(60, 50);
        executeConcurrentTicketBooking(30, 50);
        executeConcurrentTicketBooking(30, 0); // should not aquire any ticket
    }

    @Test
    void ticketBooking_onLastTicket() throws ExecutionException, InterruptedException {
        Ticket ticket = Ticket.builder()
                .capacity(new AtomicInteger(1))
                .build();
        assertTrue(ticket.acquireTicket());
        assertEquals(0, ticket.getCapacity().get());
    }

    @Test
    void ticketBooking_onZeroTicket() throws ExecutionException, InterruptedException {
        Ticket ticket = Ticket.builder()
                .capacity(new AtomicInteger(0))
                .build();
        assertFalse(ticket.acquireTicket());
        assertEquals(0, ticket.getCapacity().get());
    }

    void executeConcurrentTicketBooking(int numThreads, int initialCapacity) throws ExecutionException, InterruptedException {
        Ticket ticket = Ticket.builder()
                .capacity(new AtomicInteger(initialCapacity))
                .build();

        ExecutorService pool = Executors.newFixedThreadPool(numThreads);

        List<Future<Boolean>> futures = new ArrayList<>();
        for (int i = 0; i <= initialCapacity+10; i++) {
            futures.add(pool.submit(ticket::acquireTicket));
        }

        for (Future<Boolean> future : futures) {
            future.get();
        }

        pool.shutdown();

        assertEquals(0, ticket.getCapacity().get(),
                "Expected ticket should be zero");
    }

    @Test
    void bulkTicketBooking_whenSufficientTicketsAvaiable() throws ExecutionException, InterruptedException {
        Ticket ticket = Ticket.builder()
                .capacity(new AtomicInteger(10))
                .build();
        assertTrue(ticket.acquireTickets(10));
        assertEquals(0, ticket.getCapacity().get());
    }

    @Test
    void bulkTicketBooking_whenSufficientTicketsNotAvaiable() throws ExecutionException, InterruptedException {
        Ticket ticket = Ticket.builder()
                .capacity(new AtomicInteger(9))
                .build();
        assertFalse(ticket.acquireTickets(10));
        assertEquals(9, ticket.getCapacity().get());
    }
}
