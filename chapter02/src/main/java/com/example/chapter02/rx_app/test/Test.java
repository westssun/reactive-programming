package com.example.chapter02.rx_app.test;

import rx.Observable;
import rx.Subscriber;

import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Observable<String> observable = Observable.create(
            new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> sub) {
                    sub.onNext("Hello, reactive world!");
                    sub.onCompleted();
                }
            });

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onCompleted() {
                System.out.println("Done!");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e);
            }
        };

        observable.subscribe(subscriber);

        /* 다양한 Observable 인스턴스 생성 방식 */
        Observable.just("1", "2", "3", "4");
        Observable.from(new String[]{"A", "B", "C"});
        Observable.from(Collections.emptyList());

        /* Callable 또는 Future 를 사용할 수도 있다. */
        Observable<String> hello = Observable.fromCallable(() -> "hello");

        Future<String> future = Executors.newCachedThreadPool().submit(() -> "World");
        Observable<String> world = Observable.from(future);

        /* concat() 메서드를 사용해 입력 스트림을 다운 스트림 Observable로 다시 보낼 수 있다. */
        Observable.concat(hello, world, Observable.just("!"))
                .forEach(System.out::print);

        /* 비동기 시퀀스 생성하기 */
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(e -> System.out.println("Received : " + e));
        Thread.sleep(5000); // sleep()을 제거하면 아무것도 출력되지 않는다. 별개의 스레드에서 사용되기 때문이다.
        // 메인 스레드가 실행을 끝내지 못하도록 sleep()을 쓰거나 다른 방법을 이용해 종료를 지연시킬 수 있다.


    }
}
