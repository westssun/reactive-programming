package com.reactive.chapter02.observer;

/**
 * 관찰자
 * @param <T>
 */
public interface Observer<T> {
   /**
    * 이벤트 처리
    * @param event
    */
   void observe(T event);
}
