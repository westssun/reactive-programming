package com.reactive.chapter02.observer;

/**
 * 주체
 * @param <T>
 */
public interface Subject<T> {
   /**
    * 등록
    * @param observer
    */
   void registerObserver(Observer<T> observer);

   /**
    * 삭제
    * @param observer
    */

   void unregisterObserver(Observer<T> observer);

   /**
    * 알림
    * @param event
    */

   void notifyObservers(T event);
}
