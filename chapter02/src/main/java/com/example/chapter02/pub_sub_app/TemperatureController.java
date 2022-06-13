package com.example.chapter02.pub_sub_app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static java.lang.String.format;

@RestController
public class TemperatureController {
   static final long SSE_SESSION_TIMEOUT = 30 * 60 * 1000L;
   private static final Logger log = LoggerFactory.getLogger(TemperatureController.class);

   /**
    * CopyOnWriteArraySet
    * 목록에 대한 수정/반복을 동시에 할 수 있다.
    * 웹 클라이언트가 새로운 SSE 세션을 요청하면 clients 컬렉션에 새로운 emitter 을 추가한다.
    * SseEmitter는 처리가 끝나거나 timeout에 도달하면 clients 컬렉션에서 자신을 제거한다.
    */
   private final Set<SseEmitter> clients = new CopyOnWriteArraySet<>();

   /**
    * 접속 : http://localhost:8080/index.html/
    *
    * ResponseBodyEmitter : 메시지 컨버터에 의해 개별적으로 만들어진 여러개의 오브젝트를 전달하는 용도로 사용 그러게
    * SseEmitter : ResponseBodyEmitter 을 상속했으며, SSE의 프로토콜 요구사항에 따라 하나의 수신 요청에 대해
    * 다수의 발신 메시지를 보낼 수 있다.
    * @param request
    * @return
    */
   @GetMapping(value = "/temperature-stream")
   public SseEmitter events(HttpServletRequest request) {
      log.info("SSE stream opened for client: " + request.getRemoteAddr());

      // SseEmitter 를 SSE 이벤트를 보내는 목적으로만 사용한다.
      // SseEmitter 인스턴스를 반환하더라도 SseEmitter.complete() 메서드가 호출되거나 오류 발생/시간초과가 발생할때까지 실제 요청 처리는 계속된다.
      SseEmitter emitter = new SseEmitter(SSE_SESSION_TIMEOUT);
      clients.add(emitter);

      // Remove SseEmitter from active clients on error or client disconnect
      emitter.onTimeout(() -> clients.remove(emitter));
      emitter.onCompletion(() -> clients.remove(emitter));

      return emitter;
   }

   /**
    * 프레임워크는 온도 이벤트를 수신할 때만 handleMessage()를 호출한다.
    * @param temperature
    */
   @Async /* 비동기 실행 */
   @EventListener /* 스프링으로부터 이벤트를 수신하기 위함 */
   public void handleMessage(Temperature temperature) {
      log.info(format("Temperature: %4.2f C, active subscribers: %d",
         temperature.getValue(), clients.size()));

      List<SseEmitter> deadEmitters = new ArrayList<>();

      clients.forEach(emitter -> {
         try {
            Instant start = Instant.now();

            // SseEmitter 는 오류 처리에 대한 콜백을 제공하지 않아서, send() 수행때 발생하는 오류를 처리할 수 있다.
            emitter.send(temperature, MediaType.APPLICATION_JSON);
            log.info("Sent to client, took: {}", Duration.between(start, Instant.now()));
         } catch (Exception ignore) {
            deadEmitters.add(emitter);
         }
      });

      clients.removeAll(deadEmitters);
   }

   @ExceptionHandler(value = AsyncRequestTimeoutException.class)
   public ModelAndView handleTimeout(HttpServletResponse rsp) throws IOException {
      if (!rsp.isCommitted()) {
         rsp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
      }
      return new ModelAndView();
   }
}
