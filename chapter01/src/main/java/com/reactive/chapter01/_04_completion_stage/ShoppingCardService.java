package com.reactive.chapter01._04_completion_stage;

import com.reactive.chapter01.common.Input;
import com.reactive.chapter01.common.Output;

import java.util.concurrent.CompletionStage;

public interface ShoppingCardService {
    CompletionStage<Output> calculate(Input value);
}
