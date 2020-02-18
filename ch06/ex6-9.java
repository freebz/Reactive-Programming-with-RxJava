// Producer와 누락된 배압

Observable<Integer> myRange(int from, int count) {
    return Observable.create(subscriber -> {
	int i = from;
	while (i < from + count) {
	    if (!subscriber.isUnsubscribed()) {
		subscriber.onNext(i++);
	    } else {
		return;
	    }
	}
	subscriber.onCompleted();
    });
}


Observable<Integer> myRangeWithBackpressure(int from, int count) {
    return Observable.create(new OnSubscribeRange(from, count));
}

class OnSubscribeRange implements Observable.OnSubscribe<Integer> {

    // 생성자...

    @Override
    public void call(final Subscriber<? super Integer> child) {
	child.setProducer(new RangeProducer(child, start, end));
    }
}

class RangeProducer implements Producer {

    @Override
    public void request(long n) {
	// 여기에서 자식 구독자의 onNext()들 호출
    }
}


myRange(1, 1_000_000_000)
        .map(Dish::new)
        .onBackpressureBuffer()
        .observeOn(Schedulers.io())
        .subscribe(x -> {
		    System.out.println("Washing: " + x);
		    sleepMillis(50);
	}));


/*
Created: 1
Created: 2
Created: 3
Created: 4
Created: 8
Created: 9
Washing: 1
Created: 10
Created: 11
...
Created: 26976
Created: 26977
Washing: 15
Exception in thread "main" java.lang.OutOfMemoryError: ...
Washing: 16
    at java.util.concurrent.ConcurrentLinkedQueue.offer...
    at rx.internal.operators.OperatorOnBackpressureBuffer...
...
*/


.onBackpressureBuffer(1000, () -> log.warn("Buffer full"))


.onBackpressureDrop(dish -> log.warn("Throw away {}", dish))
