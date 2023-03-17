package com.example.demo.orderBookModule;


import com.example.demo.orderModule.Order;

public interface IOrderEntryOrderBook extends IReadOnlyOrderBook {
    void addOrder(Order order);
    void updateOrder(Order order);
    void removeOrder(Order order);
}
