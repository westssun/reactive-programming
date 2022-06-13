package com.example.chapter02.rx_app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import rx.Subscriber;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class TemperatureController {
   private static final Logger log = LoggerFactory.getLogger(TemperatureController.class);

   private final TemperatureSensor temperatureSensor;

   public TemperatureController(TemperatureSensor temperatureSensor) {
      this.temperatureSensor = temperatureSensor;
   }

   @GetMapping(value = "/temperature-stream")
   public SseEmitter events(HttpServletRequest request) {
      RxSeeEmitter emitter = new RxSeeEmitter();
      log.info("[{}] Rx SSE stream opened for client: {}",
         emitter.getSessionId(), request.getRemoteAddr());

      temperatureSensor.temperatureStream()
         .subscribe(emitter.getSubscriber());

      return emitter;
   }

   @ExceptionHandler(value = AsyncRequestTimeoutException.class)
   public ModelAndView handleTimeout(HttpServletResponse rsp) throws IOException {
      if (!rsp.isCommitted()) {
         rsp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
      }
      return new ModelAndView();
   }

   static class RxSeeEmitter extends SseEmitter {
      static final long SSE_SESSION_TIMEOUT = 30 * 60 * 1000L;
      private final static AtomicInteger sessionIdSequence = new AtomicInteger(0);

      private final int sessionId = sessionIdSequence.incrementAndGet();
      private final Subscriber<Temperature> subscriber; // 클래스 인스턴스

      RxSeeEmitter() {
         super(SSE_SESSION_TIMEOUT);

         this.subscriber = new Subscriber<Temperature>() {
            @Override
            public void onNext(Temperature temperature) {
               try {
                  /* 응답으로 SSE 클라이언트에게 다시 신호를 보낸다. */
                  RxSeeEmitter.this.send(temperature);
                  log.info("[{}] << {} ", sessionId, temperature.getValue());
               } catch (IOException e) {
                  log.warn("[{}] Can not send event to SSE, closing subscription, message: {}",
                     sessionId, e.getMessage());
                  /* 전송에 실패하면 구독자는 수신한 스트림으로부터 자신을 구독 취소한다. */
                  unsubscribe();
               }
            }

            /* 온도 스트림이 무한하며 오류를 생성할 수 없다는 것을 알고 있으므로 아래 핸들러들은 구현하지 않는다. */
            @Override
            public void onError(Throwable e) {
               log.warn("[{}] Received sensor error: {}", sessionId, e.getMessage());
            }

            @Override
            public void onCompleted() {
               log.warn("[{}] Stream completed", sessionId);
            }
         };

         /* SSE 세션 완료에 대한 정리 작업 등록 */
         onCompletion(() -> {
            log.info("[{}] SSE completed", sessionId);
            subscriber.unsubscribe();
         });

         /* SSE 시간 초과에 대한 정리 작업 등록 */
         onTimeout(() -> {
            log.info("[{}] SSE timeout", sessionId);
            subscriber.unsubscribe();
         });
      }

      /**
       * 가입자 노출
       * @return
       */
      Subscriber<Temperature> getSubscriber() {
         return subscriber;
      }

      int getSessionId() {
         return sessionId;
      }
   }

}
