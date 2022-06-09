package com.reactive.chapter01._02_callbacks;

import com.reactive.chapter01.common.Input;

public class OrdersService {
    private final ShoppingCardService shoppingCardService;

    public OrdersService(ShoppingCardService shoppingCardService) {
        this.shoppingCardService = shoppingCardService;
    }

    void process() {
        Input input = new Input();

        /*
            비동기적으로 shoppingCardService를 호출하고, 이후 작업을 진행한다.
            shoppingCardService가 콜백 함수를 실행하면 실제 결과에 대한 처리를 계속 할 수 있다.
         */
        shoppingCardService.calculate(input, output -> {
            // 함수형 콜백 호출을 위해 동기 또는 비동기 방식으로 calculate 메서드를 구현 */
            System.out.println(shoppingCardService.getClass().getSimpleName() + " execution completed");
        });
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        // AsyncShoppingCardService 의 응답을 기다리지 않고 즉시 다른 작업을 진행한다. */
        OrdersService ordersServiceAsync = new OrdersService(new AsyncShoppingCardService());
        ordersServiceAsync.process();
        ordersServiceAsync.process();

        OrdersService ordersServiceSync = new OrdersService(new SyncShoppingCardService());
        ordersServiceSync.process();

        /*
            SyncShoppingCardService execution completed
            Total elapsed time in millis is : 46
            AsyncShoppingCardService execution completed
            AsyncShoppingCardService execution completed
         */
        System.out.println("Total elapsed time in millis is : " + (System.currentTimeMillis() - start));

        Thread.sleep(1000);
    }
}
