// 측정과 모니터링

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import org.slf4j.LoggerFactory;

MetricRegistry metricRegistry = new MetricRegistry();
Slf4jReporter reporter = Slf4jReporter
    .forRegistry(metricRegistry)
    .outputTo(LoggerFactory.getLogger(SomeClass.class))
    .build();
reporter.start(1, TimeUnit.SECONDS);


final Counter items = metricRegistry.counter("items");
observable
        .doOnNext(x -> items.inc())
        .subscribe(...);


Observable<Long> makeNetworkCall(long x) {
    //...
}

Counter counter = metricRegistry.counter("counter");
observable
        .doOnNext(x -> counter.inc())
        .flatMap(this::makeNetworkCall)
        .doOnNext(x -> counter.dec())
        .subscribe(...);


observable
    .flatMap(x ->
	makeNetworkCall(x)
	    .doOnSubscribe(counter::inc)
	    .doOnTerminate(counter::dec)
    )
    .subscribe(...);


import com.codahale.metrics.Timer;

Timer timer = metricRegistry.timer("timer");
Timer.Context ctx = timer.time();
// 시간이 오래 걸리는 작업...
ctx.stop();


Observable<Long> external = //...

Timer timer = metricRegistry.timer("timer");

Observable<Long> externalWithTimer = Observable
        .defer(() -> Observable.just(timer.time()))
        .flatMap(timerCtx ->
	        external.doOnCompleted(timerCtx::stop));
