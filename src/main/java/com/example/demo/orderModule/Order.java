package com.example.demo.orderModule;

import lombok.*;

import java.math.BigInteger;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    public BigInteger price;
    public Integer initialQty;
    public Integer currentQty;
    public OrderSide orderSide;

    public Long orderId;
    public String uid;
    public String refId;

    public boolean equals(Order o) {
        return refId.equals(o.refId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refId);
    }
}