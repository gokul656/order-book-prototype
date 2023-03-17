package com.example.demo.orderModule;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Limit implements Comparable<Limit> {

    private final BigInteger price;
    public OrderBookEntry head; // Point to first order in Queue
    public OrderBookEntry tail; // Point to last order in Queue
    public OrderSide orderSide;

    public Limit(BigInteger price) {
        this.price = price;
    }

    // To check whether any data is the particular limit for say ( check orders in limit of 1500USDT )
    public boolean isEmpty() {
        return head == null && tail == null;
    }

    public OrderSide getSide() {
        if (isEmpty()) return OrderSide.UNKNOWN;
        else return head.currentOrder.orderSide;
    }

    // Get number of order in orderbook
    public int getOrderCount() {
        int orderCount = 0;
        OrderBookEntry headPointer = head;

        while (headPointer != null) {
            if (headPointer.currentOrder.currentQty != 0) {
                orderCount++;
                headPointer = head.next;
            }
        }

        return orderCount;
    }

    // Get orderbook volume
    public int getOrderVolume() {
        int orderVolume = 0;
        OrderBookEntry headPointer = head;

        while (headPointer != null) {
            if (headPointer.currentOrder.currentQty != 0) {
                orderVolume += headPointer.currentOrder.currentQty;
                headPointer = head.next;
            }
        }

        return orderVolume;
    }

    // Returns all orders in this limit
    public List<OrderRecord> orderRecords() {
        List<OrderRecord> orderRecords = new ArrayList<>();
        OrderBookEntry headPointer = head;
        int queuePosition = 0;

        while (headPointer != null) {
            queuePosition++;

            var currentOrder = headPointer.currentOrder;
            if (currentOrder.currentQty != 0) {
                orderRecords.add(new OrderRecord(
                        currentOrder.price,
                        currentOrder.currentQty,
                        currentOrder.orderSide,
                        queuePosition,
                        currentOrder.orderId,
                        currentOrder.uid
                ));

                queuePosition++;
            }
            headPointer = head.next;
        }

        return orderRecords;
    }

    public BigInteger getPrice() {
        return price;
    }

    @Override
    public int compareTo(Limit o) {
        return price.compareTo(o.price);
    }
}