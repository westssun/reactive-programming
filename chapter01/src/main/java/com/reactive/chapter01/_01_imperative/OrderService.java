package com.reactive.chapter01._01_imperative;

import com.reactive.chapter01.common.Input;
import com.reactive.chapter01.common.Output;

public class OrderService {
    private final ShoppingCardService scService;

    public OrderService(ShoppingCardService scService) {
        this.scService = scService;
    }

    void process() {
        Input input = new Input();

        /* 동기 호출 : OrderService 비즈니스 로직을 처리하는 동안 스레드가 차단된다. */
        Output output = scService.calculate(input);

        System.out.println(scService.getClass().getSimpleName() + " execution completed");
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        new OrderService(new BlockingShoppingCardService()).process();
        new OrderService(new BlockingShoppingCardService()).process();

        /*
            BlockingShoppingCardService execution completed
            BlockingShoppingCardService execution completed
         */
        System.out.println("Total elapsed time in millis is : " + (System.currentTimeMillis() - start));
    }
}
