package com.example.chapter02.pub_sub_app;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class TemperatureSensor {
   /* 이벤트 발행 */
   private final ApplicationEventPublisher publisher;

   /* 온도 (난수로 생성) */
   private final Random rnd = new Random();

   /* 이벤트 생성 프로세스 */
   private final ScheduledExecutorService executor =
           Executors.newSingleThreadScheduledExecutor();

   public TemperatureSensor(ApplicationEventPublisher publisher) {
      this.publisher = publisher;
   }

   /**
    * 빈 생성시 실행
    */
   @PostConstruct
   public void startProcessing() {
      this.executor.schedule(this::probe, 1, SECONDS);
   }

   private void probe() {
      double temperature = 16 + rnd.nextGaussian() * 10;
      publisher.publishEvent(new Temperature(temperature));

      // 이벤트 생성 예약 (임의의 지연시간)
      executor.schedule(this::probe, rnd.nextInt(5000), MILLISECONDS);
   }
}
