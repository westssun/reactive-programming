package com.reactive.chapter02.observer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Subject 구현체
 */
public class ConcreteSubject implements Subject<String> {
   /*
      아래 메서드를 통해 observers 의 수정이 가능하다. (구독/구독취소)

      스레드 안정성 : CopyOnWriteArraySet 사용
      - 업데이트 작업이 발생할때마다 새 복사본을 생성하는 Set 구현체
    */
   private final Set<Observer<String>> observers = new CopyOnWriteArraySet<>();

   /**
    * 관찰자 등록
    * @param observer
    */
   public void registerObserver(Observer<String> observer) {
      observers.add(observer);
   }

   /**
    * 관찰자 삭제
    * @param observer
    */
   public void unregisterObserver(Observer<String> observer) {
      observers.remove(observer);
   }

   /**
    * 이벤트 수행
    * @param event
    */
   public void notifyObservers(String event) {
      observers.forEach(observer -> observer.observe(event));
   }
}
