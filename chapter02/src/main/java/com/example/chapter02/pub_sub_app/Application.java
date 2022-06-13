package com.example.chapter02.pub_sub_app;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 실행 클래스
 */
@EnableAsync /* 비동기 실행 가능한 스프링 부트 어플리케이션 */
@SpringBootApplication
public class Application implements AsyncConfigurer {

   public static void main(String[] args) {
      SpringApplication.run(Application.class, args);
   }

   @Override
   public Executor getAsyncExecutor() {
      ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
      executor.setThreadNamePrefix("sse-");
      executor.setCorePoolSize(2);
      executor.setMaxPoolSize(100); // 최대 100개의 쓰레드까지 증가
      executor.setQueueCapacity(5);
      executor.initialize();
      return executor;
   }

   @Override
   public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
      return new SimpleAsyncUncaughtExceptionHandler();
   }
}
