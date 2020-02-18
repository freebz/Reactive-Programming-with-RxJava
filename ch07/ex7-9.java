// 단위 테스트에서의 Scheduler

TestScheduler sched = Scheduler.test();
Observable<String> fast = Observable
    .interval(10, MILLISECONDS, sched)
    .map(x -> "F" + x)
    .take(3);
Observable<String> slow = Observable
    .interval(50, MILLISECONDS, sched)
    .map(x -> "S" + x);

Observable<String> stream = Observable.concat(fast, slow);
stream.subscribe(System.out::println);
System.out.println("Subscribed");


TimeUnit.SECONDS.sleep(1);
System.out.println("After one second");
sched.advanceTimeBy(25, MILLISECONDS);

TimeUnit.SECONDS.sleep(1);
System.out.println("After one more second");
sched.advanceTimeBy(75, MILLISECONDS);

TimeUnit.SECONDS.sleep(1);
System.out.println("...and one more");
sched.advanceTimeBy(200, MILLISECONDS);


/*
Subscribed
After one second
F0
F1
After one more second
F2
S0
...and one more
S1
S2
*/
