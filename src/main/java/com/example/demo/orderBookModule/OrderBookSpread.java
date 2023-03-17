package com.example.demo.orderBookModule;

import java.math.BigInteger;

// What if there is no bids in OB?
// What if there is no asks in OB?
public class OrderBookSpread {
    private final BigInteger bid;
    private final BigInteger ask;

    public OrderBookSpread(BigInteger bid, BigInteger ask) {
        this.bid = bid;
        this.ask = ask;
    }

    public BigInteger spread() {
        if (bid == null || ask == null) return null;
        return bid.subtract(ask);
    }
}
