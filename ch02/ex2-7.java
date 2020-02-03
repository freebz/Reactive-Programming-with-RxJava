// 무한 스트림

// 잘못된 구현! 이렇게 하지 말자
Observable<BigInteger> naturalNumbers = Observable.create(
        subscriber -> {
	    BigInteger i = ZERO;
	    while (true) { // 이렇게 하지 말자!
		subscriber.onNext(i);
		i = i.add(ONE);
	    }
	});
naturalNumbers.subscribe(x -> log(x));


Observable<BigInteger> naturalNumbers = Observable.create(
    subscriber -> {
	Runnable r = () -> {
	    BitInteger i = ZERO;
	    while (!subscriber.isUnsubscribed()) {
		subscriber.onNext(i);
		i = i.add(ONE);
	    }
	};
	new Thread(r).start();
    });


Subscription subscription = naturalNumbers.subscribe(x -> log(x));
// 시간이 어느 정도 지난 다음...
subscription.unsubscribe();


static <T> Observable<T> delayed(T x) {
    return Observable.create(
	subscriber -> {
	    Runnable r = () -> {
		sleep(10, SECONDS);
		if (!subscriber.isUnsubscribed()) {
		    subscriber.onNext(x);
		    subscriber.onCompleted();
		}
	    };
	    new Thread(r).start();
	});
}

static void sleep(int timeout, TimeUnit unit) {
    try {
	unit.sleep(timeout);
    } catch (InterruptedException ignored) {
	// 일부러 무시한 부분
    }
}


static <T> Observable<T> delayed(T x) {
    return Observable.create(
	subscriber -> {
	    Runnable r = () -> {/* ... */};
	    final Thread thread = new Thread(r);
	    thread.start();
	    subscriber.add(Subscriptions.create(thread::interrupt));
	});
}


Observable<Data> loadAll(Collection<Integer> ids) {
    return Observable.create(subscriber -> {
	ExecutorService pool = Executors.newFixedThreadPool(10);
	AtomicInteger countDown = new AtomicInteger(ids.size());
	// 위험, Rx 계약 위반, 이렇게 하면 안된다!
	ids.forEach(id -> pool.submit(() -> {
	    final Data data = load(id);
	    subscriber.onNext(data);
	    if (countDown.decrementAngGet() == 0) {
		pool.shutdownNow();
		subscriber.onCompleted();
	    }
	}));
    });
}


Observable<Data> rxLoad(int id) {
    return Observable.create(subscriber -> {
	try {
	    subscriber.onNext(load(id));
	    subscriber.onCompleted();
	} catch (Exception e) {
	    subscriber.onError(e);
	}
    });
}


Observable<Data> rxLoad(int id) {
    return Observable.fromCallable(() ->
        load(id));
}
