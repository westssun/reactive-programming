package com.reactive.chapter01._03_futures;

import com.reactive.chapter01.common.Input;
import com.reactive.chapter01.common.Output;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class OrdersService {
    private final ShoppingCardService shoppingCardService;

    public OrdersService(ShoppingCardService shoppingCardService) {
        this.shoppingCardService = shoppingCardService;
    }

    void process() {
        Input input = new Input();

        /*
            비동기적으로 calculate를 호출하고 Future 인스턴스를 반환한다.
            비동기적으로 처리되는 동안 다른 처리를 계속할 수 있다.
         */
        Future<Output> result = shoppingCardService.calculate(input);

        System.out.println(shoppingCardService.getClass().getSimpleName() + " execution completed");

        try {
            /* 블로킹 방식으로 결과를 기다리거나, 즉시 결과를 반환할 수 있다. */
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        OrdersService ordersService1 = new OrdersService(new FutureShoppingCardService());

        ordersService1.process();
        ordersService1.process();

        /*
            FutureShoppingCardService execution completed
            FutureShoppingCardService execution completed
            Total elapsed time in millis is : 2032
         */
        System.out.println("Total elapsed time in millis is : " + (System.currentTimeMillis() - start));
    }
}
