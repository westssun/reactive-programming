package com.example.chapter02.rx_app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.util.Random;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component // 빈 등록
public class TemperatureSensor {
   private static final Logger log = LoggerFactory.getLogger(TemperatureSensor.class);
   private final Random rnd = new Random();

   /* dataStream : 컴포넌트에서 정의한 유일한 Observable 스트림 */
   private final Observable<Temperature> dataStream =
      Observable
         .range(0, Integer.MAX_VALUE) // 무한대 숫자 생성
         /* 각 측정 사이의 최대 간격이 5초인 센서 값을 반환하는 스트림 생성 */
         .concatMap(ignore -> Observable // 값 변환
            .just(1) // 새로운 스트림 생성
            .delay(rnd.nextInt(5000), MILLISECONDS) // 지연 후
            .map(ignore2 -> this.probe())) // 센서 측정 수행
         .publish() // 모든 대상 스트림으로 브로드캐스팅 (ConnectableObservable 타입 반환)
         /* ConnectableObservable : 적어도 하나 이상의 구독자가 있을때만 입력 공유 스트림에 대한 구독을 생성하는 refCount() 연산자를 제공한다. */
         .refCount();

   public Observable<Temperature> temperatureStream() {
     return dataStream;
   }

   private Temperature probe() {
      double actualTemp = 16 + rnd.nextGaussian() * 10;
      log.info("Asking sensor, sensor value: {}", actualTemp);
      return new Temperature(actualTemp);
   }
}
