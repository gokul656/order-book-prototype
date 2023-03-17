package com.example.demo.orderModule;

import com.example.demo.orderBookModule.OrderBook;
import com.example.demo.orderBookModule.Security;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.UUID;

@Slf4j
public class DemoApplication {

	public static void main(String[] args) {
		OrderBook orderBook = new OrderBook(new Security());

		Order bidOrder = Order.builder()
				.price(new BigInteger("100"))
				.initialQty(10)
				.refId(UUID.randomUUID().toString())
				.orderSide(OrderSide.BUY)
				.build();
		Order askOrder = Order.builder()
				.price(new BigInteger("100"))
				.initialQty(10)
				.orderSide(OrderSide.SELL)
				.build();

		orderBook.addOrder(bidOrder);
		orderBook.addOrder(askOrder);

		log.info("{}", orderBook.getBidOrders().size());
		log.info("{}", orderBook.getAskOrders().size());
	}
}
