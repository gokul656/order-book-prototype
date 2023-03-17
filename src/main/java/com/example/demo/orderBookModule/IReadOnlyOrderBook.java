package com.example.demo.orderBookModule;

public interface IReadOnlyOrderBook {
    boolean containsOrder(Long orderId);
    OrderBookSpread getSpread();
    int getOrderCount();
}
