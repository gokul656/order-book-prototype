package com.example.demo.orderBookModule;

public interface IMatchingOrderBook extends IOrderBookRetrieval {
    MatchResult match();
}
