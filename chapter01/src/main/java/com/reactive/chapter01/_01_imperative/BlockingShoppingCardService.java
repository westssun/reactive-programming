package com.reactive.chapter01._01_imperative;


import com.reactive.chapter01.common.Input;
import com.reactive.chapter01.common.Output;

public class BlockingShoppingCardService implements ShoppingCardService {
    @Override
    public Output calculate(Input value) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new Output();
    }
}
