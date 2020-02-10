// Scheduler란 무엇인가?


// Schedulers.newThread()
// Schedulers.io()
// Schedulers.computation()
// Schedulers.from(Executor excutor)

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

//...

ThreadFactory threadFactory = new ThreadFactoryBuilder()
    .setNameFormat("MyPool-%d")
    .build();
Executor executor = new ThreadPoolExecutor(
    10, //corePoolSize
    10, //maximumPoolSize
    OL, TimeUnit.MILLISECONDS, //keepAliveTime, unit
    new LinkedBlockingQueue<>(1000), //workQueue
    threadFactory
);
Scheduler scheduler = Schedulers.from(executor);


import java.util.concurrent.Executors;

//...

ExecutorService executor = Executor.newFixedThreadPool(10);


// Schedulers.immediate()
// Schedulers.trampoline()

Scheduler scheduler = Schedulers.immediate();
Scheduler.Worker worker = scheduler.createWorker();

log("Main start");
worker.schedule(() -> {
    log(" Outer start");
    sleepOneSecond();
    worker.schedule(() -> {
	log(" Inner start");
	sleepOneSecond();
	log(" Inner end");
    });
    log(" Outer end");
});
log("Main end");
worker.unsubscribe();


/*
1044   | main  | Main start
1094   | main  |  Outer start
2097   | main  |   Inner start
3097   | main  |   Inner end
3100   | main  |  Outer end
3100   | main  | Main end
*/


Scheduler scheduler = Schedulers.trampoline();


/*
1030   | main  | Main start
1096   | main  |  Outer start
2101   | main  |  Outer end
2101   | main  |   Inner start
3101   | main  |   Inner end
3101   | main  | Main end
*/


log("Main start");
worker.schedule(() -> {
    log(" Outer start");
    sleepOneSecond();
    worker.schedule(() -> {
	log("  Middle start");
	sleepOneSecond();
	worker.schedule(() -> {
	    log("   Inner start");
	    sleepOneSecond();
	    log("   Inner end");
	});
	log("  Middle end");
    });
    log(" Outer end");
});
log("Main end");


/*
1029   | main  | Main start
1091   | main  |  Outer start
2093   | main  |   Middle start
3095   | main  |    Inner start
4096   | main  |    Inner end
4099   | main  |   Middle end
4099   | main  |  Outer end
4099   | main  | Main end
*/


/*
1041   | main  | Main start
1095   | main  |  Outer start
2099   | main  |  Outer end
2099   | main  |   Middle start
3101   | main  |   Middle end
3101   | main  |    Inner start
4102   | main  |    Inner end
4102   | main  | Main end
*/


// Schedules.test()
