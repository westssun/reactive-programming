package com.reactive.chapter01._02_callbacks;

import com.reactive.chapter01.common.Input;
import com.reactive.chapter01.common.Output;

import java.util.function.Consumer;

/**
 * 비동기적 수행
 */
public class AsyncShoppingCardService implements ShoppingCardService {

    @Override
    public void calculate(Input value, Consumer<Output> c) {
        new Thread(() -> { /* 별도의 Thread로 래핑 */
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /* 결과를 받으면 콜백 함수를 호출한다. */
            c.accept(new Output());
        }).start();
    }
}
