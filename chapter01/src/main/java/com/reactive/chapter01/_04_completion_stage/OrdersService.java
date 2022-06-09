package com.reactive.chapter01._04_completion_stage;

import com.reactive.chapter01.common.Input;

public class OrdersService {
    private final ShoppingCardService shoppingCardService;

    public OrdersService(ShoppingCardService shoppingCardService) {
        this.shoppingCardService = shoppingCardService;
    }

    void process() {
        Input input = new Input();

        shoppingCardService.calculate(input) // 호출 후 결과 CompletionStage 는 즉시 반환받는다.
                            // 결과에 대한 변형 연산 정의
                           .thenAccept(v -> System.out.println(shoppingCardService.getClass().getSimpleName() + " execution completed"));

        System.out.println(shoppingCardService.getClass().getSimpleName() + " calculate called");
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        OrdersService ordersService1 = new OrdersService(new CompletionStageShoppingCardService());

        ordersService1.process();
        ordersService1.process();

        /*
            CompletionStageShoppingCardService calculate called
            CompletionStageShoppingCardService calculate called
            Total elapsed time in millis is : 45
            CompletionStageShoppingCardService execution completed
            CompletionStageShoppingCardService execution completed
         */
        System.out.println("Total elapsed time in millis is : " + (System.currentTimeMillis() - start));

        Thread.sleep(1000);
    }
}
