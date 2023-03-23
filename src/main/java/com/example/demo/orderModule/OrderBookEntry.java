package com.example.demo.orderModule;

import lombok.NoArgsConstructor;
import lombok.ToString;

import java.security.Timestamp;

@NoArgsConstructor
@ToString
public class OrderBookEntry {
    public Timestamp ts;
    public Order currentOrder;
    public Limit parentLimit;

    public OrderBookEntry next;
    public OrderBookEntry previous;

    public OrderBookEntry(Order order, Limit parentLimit) {
        this.currentOrder = order;
        this.parentLimit = parentLimit;
    }
}