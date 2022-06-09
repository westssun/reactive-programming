package com.reactive.chapter01._02_callbacks;

import com.reactive.chapter01.common.Input;
import com.reactive.chapter01.common.Output;

import java.util.function.Consumer;

public interface ShoppingCardService {
    /* 호출하는 인스턴스가 즉시 대기 상태에서 해제될 수 있으며, 그 결과는 나중에 지정된 Consumer<> 콜백으로 전달된다. */
    void calculate(Input value, Consumer<Output> c);
}
