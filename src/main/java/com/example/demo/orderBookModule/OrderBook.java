package com.example.demo.orderBookModule;

import com.example.demo.orderModule.Limit;
import com.example.demo.orderModule.Order;
import com.example.demo.orderModule.OrderBookEntry;
import com.example.demo.orderModule.OrderSide;

import java.math.BigInteger;
import java.util.*;

public class OrderBook implements IOrderBookRetrieval {

    private final Security security;

    private HashMap<Long, OrderBookEntry> orders = new HashMap<>();
    private SortedSet<Limit> bidLimits = new TreeSet<>();
    private SortedSet<Limit> askLimits = new TreeSet<>();

    public OrderBook(Security security) {
        this.security = security;
    }

    @Override
    public List<OrderBookEntry> getBidOrders() {
        List<OrderBookEntry> orderBookEntries = new ArrayList<>();
        bidLimits.forEach(
                askLimit -> {
                    if (!askLimit.isEmpty()) {
                        OrderBookEntry bidLimitPointer = askLimit.head;
                        while (bidLimitPointer != null) {
                            orderBookEntries.add(bidLimitPointer);
                            bidLimitPointer = bidLimitPointer.next;
                        }
                    }
                }
        );

        return orderBookEntries;
    }

    @Override
    public List<OrderBookEntry> getAskOrders() {
        List<OrderBookEntry> orderBookEntries = new ArrayList<>();
        askLimits.forEach(
                askLimit -> {
                    if (!askLimit.isEmpty()) {
                        OrderBookEntry askLimitPointer = askLimit.head;
                        while (askLimitPointer != null) {
                            orderBookEntries.add(askLimitPointer);
                            askLimitPointer = askLimitPointer.next;
                        }
                    }
                }
        );

        return orderBookEntries;
    }

    @Override
    public void addOrder(Order order) {
        Limit baseLimit = new Limit(order.price);
        addOrder(order, baseLimit, order.orderSide == OrderSide.BUY ? bidLimits : askLimits, orders);
    }

    /**
     * @param baseLimit - Parent limit
     */
    private void addOrder(Order order, Limit baseLimit, SortedSet<Limit> limitLevels, HashMap<Long, OrderBookEntry> internalOrderBook) {
        Limit limit = limitLevels.contains(baseLimit) ? limitLevels.stream().filter(it -> it.head.equals(baseLimit.head)).findFirst().orElse(null) : null;
        if (limit != null) {
            OrderBookEntry orderBookEntry = new OrderBookEntry(order, baseLimit);

            // If by some cause the head is deleted
            if (limit.head == null) {
                limit.head = orderBookEntry;
            } else {
                OrderBookEntry tailPointer = limit.tail;
                tailPointer.next = orderBookEntry;
                orderBookEntry.previous = tailPointer;
            }

            limit.tail = orderBookEntry;
            internalOrderBook.put(order.orderId, orderBookEntry);
        } else {
            limitLevels.add(baseLimit);
            OrderBookEntry orderBookEntry = new OrderBookEntry(order, baseLimit);

            // Tail and head points to same order because it's the only order in the orderbook
            baseLimit.head = orderBookEntry;
            baseLimit.tail = orderBookEntry;
            internalOrderBook.put(order.orderId, orderBookEntry);
        }
    }


    @Override
    public void updateOrder(Order modifyingOrder) {
        OrderBookEntry orderBookEntry = orders.get(modifyingOrder.orderId);
        if (orderBookEntry != null) {
            removeOrder(modifyingOrder);
            addOrder(modifyingOrder, orderBookEntry.parentLimit, modifyingOrder.orderSide == OrderSide.BUY ? bidLimits : askLimits, orders);
        }
    }

    @Override
    public void removeOrder(Order cancelOrder) {
        OrderBookEntry orderBookEntry = orders.get(cancelOrder.orderId);
        if (orderBookEntry != null) {
            removeOrder(cancelOrder.orderId, orderBookEntry, orders);
        }
    }

    private void removeOrder(Long orderId, OrderBookEntry orderBookEntry, HashMap<Long, OrderBookEntry> internalOrderBook) {
        // Rewiring prev and next orders
        if (orderBookEntry.previous != null && orderBookEntry.next != null) {
            orderBookEntry.next.previous = orderBookEntry.previous;
            orderBookEntry.previous.next = orderBookEntry.next;
        } else if (orderBookEntry.previous != null) {
            orderBookEntry.previous.next = null;
        } else if (orderBookEntry.next != null) {
            orderBookEntry.next.previous = null;
        }

        if (orderBookEntry.parentLimit.head == orderBookEntry && orderBookEntry.parentLimit.tail == orderBookEntry) {
            // Deal with only one order
            orderBookEntry.parentLimit.head = null;
            orderBookEntry.parentLimit.tail = null;
        } else if (orderBookEntry.parentLimit.head == orderBookEntry) {
            // More than one order, but Orderbook entry is the first level
            orderBookEntry.parentLimit.head = orderBookEntry.next;
        } else if (orderBookEntry.parentLimit.tail == orderBookEntry) {
            // More than one order, but Orderbook entry is the first level
            orderBookEntry.parentLimit.tail = orderBookEntry.previous;
        }

        internalOrderBook.remove(orderId);
    }

    @Override
    public boolean containsOrder(Long orderId) {
        return orders.containsKey(orderId);
    }

    @Override
    public OrderBookSpread getSpread() {
        BigInteger bestAsk = null;
        BigInteger bestBid = null;

        if (!askLimits.isEmpty() && !Collections.min(askLimits).isEmpty()) {
            bestAsk = Collections.min(askLimits).getPrice();
        }

        if (!bidLimits.isEmpty() && !Collections.max(bidLimits).isEmpty()) {
            bestBid = Collections.max(bidLimits).getPrice();
        }

        return new OrderBookSpread(bestAsk, bestBid);
    }

    @Override
    public int getOrderCount() {
        return orders.size();
    }
}
