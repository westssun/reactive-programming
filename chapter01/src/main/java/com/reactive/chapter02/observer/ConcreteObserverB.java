package com.reactive.chapter02.observer;

/**
 * Observer 구현체
 */
public class ConcreteObserverB implements Observer<String> {
   /**
    * Observer
    * @param event
    */
   @Override
   public void observe(String event) {
      System.out.println("Observer B: " + event);
   }
}
