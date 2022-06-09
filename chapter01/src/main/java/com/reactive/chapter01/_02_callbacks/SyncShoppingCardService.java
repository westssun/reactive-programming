package com.reactive.chapter01._02_callbacks;

import com.reactive.chapter01.common.Input;
import com.reactive.chapter01.common.Output;

import java.util.function.Consumer;

/**
 * 동기적 수행
 */
public class SyncShoppingCardService implements ShoppingCardService {
    @Override
    public void calculate(Input value, Consumer<Output> c) {
        c.accept(new Output());
    }
}
