package com.example.chapter02.observer;

/**
 * Observer 구현체
 */
public class ConcreteObserverA implements Observer<String> {
   /**
    * 이벤트 수신 후 처리 로직
    * @param event
    */
   @Override
   public void observe(String event) {
      System.out.println("Observer A: " + event);
   }
}
