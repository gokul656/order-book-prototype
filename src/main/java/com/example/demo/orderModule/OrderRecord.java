package com.example.demo.orderModule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRecord {
    public BigInteger price;
    public Integer quantity;
    public OrderSide orderSide;

    public Integer queuePosition;
    public Long orderId;
    public String uid;
}
