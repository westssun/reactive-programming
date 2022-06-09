package com.reactive.chapter01._03_futures;

import com.reactive.chapter01.common.Input;
import com.reactive.chapter01.common.Output;

import java.util.concurrent.Future;

public interface ShoppingCardService {
    Future<Output> calculate(Input value);
}
