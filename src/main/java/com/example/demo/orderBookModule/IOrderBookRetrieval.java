package com.example.demo.orderBookModule;

import com.example.demo.orderModule.OrderBookEntry;

import java.util.List;

public interface IOrderBookRetrieval extends IOrderEntryOrderBook {
    List<OrderBookEntry> getBidOrders();
    List<OrderBookEntry> getAskOrders();
}
