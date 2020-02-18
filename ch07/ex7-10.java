// 단위 테스트

// 방출 이벤트 검증하기

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

@Test
public void shouldApplyConcatMapInOrder() throws Exception {
    List<String> list = Observable
	.range(1, 3)
	.concatMap(x -> Observable.just(x, -x))
	.map(Object::toString)
	.toList()
	.toBlocking()
	.single();

    assertThat(list).containsExactly("1", "-1", "2", "-2", "3", "-3");
}


import com.google.common.io.Files;
import static java.nio.charset.StandardCharsets.UTF_8;
import
    static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

File file = new File("404.txt");
BlockingObservable<String> fileContents = Observable
    .fromCallable(() -> Files.toString(file, UTF_8))
    .toBlocking();

try {
    fileContents.single();
    failBecauseExceptionWasNotThrown(FileNotFoundException.class);
} catch (RuntimeException expected) {
    assertThat(expected)
	.hasCauseInstanceOf(FileNotFoundException.class);
}


import static rx.Observable.fromCallable;

Observable<Notification<Integer>> notifications = Observable
    .just(3, 0, 2, 0, 1, 0)
    .concatMapDelayError(x -> fromCallable(() -> 100 / x))
    .materialize();

List<Notification.Kind> kinds = notifications
    .map(Notification::getKind)
    .toList()
    .toBlocking()
    .single();

assertThat(kinds).containsExactly(OnNext, OnNext, OnNext, OnError);


Observable<Integer> obs = Observable
        .just(3, 0, 2, 0, 1, 0)
        .concatMapDelayError(x -> Observable.fromCallable(() -> 100 / x));

TestSubscriber<Integer> ts = new TestSubscriber();
obs.subscribe(ts);

ts.assertValues(33, 50, 100);
ts.assertError(ArithmeticException.class);  // 실패한뎌(!)


interface MyService {
    Observable<LocalDate> externalCall();
}


class MyServiceWithTimeout implements MyService {

    private final MyService delegate;
    private final Scheduler scheduler;

    MyServiceWithTimeout(MyService d, Scheduler s) {
	this.delegate = d;
	this.scheduler = s;
    }

    @Override
    public Observable<LocalDate> externalCall() {
	return delegate
 	        .externalCall()
	        .timeout(1, TimeUnit.SECONDS,
		    Observable.empty(),
		    scheduler);
    }
}


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

private MyServiceWithTimeout mockReturning(
            Observable<LocalDate> result,
	    TestScheduler testScheduler) {
    MyService mock = mock(MyService.class);
    given(mock.externalCall()).withReturn(result);
    return new MyServiceWithTimeout(mock, testScheduler);
}


@Test
public void timeoutWhenServiceNeverCompletes() throws Exception {
    //given
    TestScheduler testScheduler = Schedulers.test();
    MyService mock = mockReturning(
 	    Observable.never(), testScheduler);
    TestSubscriber<LocalDate> ts = new TestSubscriber<>();

    //When
    mock.externalCall().subscribe(ts);

    //then
    testScheduler.advanceTimeBy(950, MILLISECONDS);
    ts.assertNoTerminalEvent();
    testScheduler.advanceTimeBy(100, MILLISECONDS);
    ts.assertCompleted();
    ts.assertNoValues();
}


@Test
public void valueIsReturnedJustBeforeTimeout() throws Exception {
    //given
    TestScheduler testScheduler = Schedulers.test();
    Observable<LocalDate> slow = Observable
	    .timer(950, MILLISECONDS, testScheduler)
  	    .map(x -> LocalDate.now());
    MyService myService = mockReturning(slow, testScheduler);
    TestSubscriber<LocalDate> ts = new TestSubscriber<>();

    //when
    myService.externalCall().subscribe(ts);

    //then
    testScheduler.advanceTimeBy(930, MILLISECONDS);
    ts.assertNotCompleted();
    ts.assertNoValues();
    testScheduler.advanceTimeBy(50, MILLISECONDS);
    ts.assertCompleted();
    ts.assertValueCount(1);
}


private final TestScheduler testScheduler = new TestScheduler();

@Before
public void alwaysUseTestScheduler() {
    RxJavaPlugins
	.getInstance()
	.registerSchedulersHook(new RxJavaSchedulersHook() {
	    @Override
	    public Scheduler getComputationScheduler() {
		return testScheduler;
	    }

	    @Override
	    public Scheduler getIOScheduler() {
		return testScheduler;
	    }

	    @Override
	    public Scheduler getNewThreadScheduler() {
		return testScheduler;
	    }
        });
}


Observable<Long> naturals1() {
    return Observable.create(subscriber -> {
	long i = 0;
	while (!subscriber.isUnsubscribed()) {
	    subscriber.onNext(i++);
	}
    });
}


Observable<Long> naturals2() {
    return Observable.create(
	SyncOnSubscribe.createStateful(
	    () -> 0L,
	    (cur, observer) -> {
		observer.onNext(cur);
		return cur + 1;
	    }
    });
}


TestSubscriber<Long> ts = new TestSubscriber<>(0);

naturals1()
        .take(10)
        .subscribe(ts);

ts.assertNoValues();
ts.requestMore(100);
ts.assertValueCount(10);
ts.assertCompleted();


/*
AssertionError: No onNext events expected yet some received: 10
*/
