// 배압

// RxJava의 배압

class Dish {
    private final byte[] oneKb = new byte[1_024];
    private final int id;

    Dish(int id) {
	this.id = it;
	System.out.println("Created: " + id);
    }

    public String toString() {
	return String.valueOf(id);
    }
}


Observable<Dish> dishes = Observable
    .range(1, 1_000_000_000)
    .map(Dish::new);


Observable
    .range(1, 1_000_000_000)
    .map(Dish::new)
    .subscribe(x -> {
        System.out.println("Washing: " + x);
	sleepMillis(50);
    });


/*
Created: 1
Washing: 1
Created: 2
Washing: 2
Created: 3
Washing: 3
...
Created: 110
Washing: 110
...
*/


dishes
    .observeOn(Schedulers.io())
    .subscribe(x -> {
        System.out.println("Washing: " + x);
	sleepMillis(50);
    });


/*
Created: 1
Created: 2
Created: 3
...
Created: 128

Washing: 1
Washing: 2
...
Washing: 128

Created: 129
...
Created: 223
Created: 224

Washing: 129
Washing: 130
...
*/


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


myRange(1, 1_000_000_000)
        .map(Dish::new)
        .observeOn(Schedulers.io())
        .subscribe(x -> {
		    System.out.println("Washing: " + x);
		    sleepMillis(50);
	        },
	        Throwable::printStackTrace
	);


/*
Created: 1
Created: 2
Created: 3
...
Created: 7177
Created: 7178

rx.exception.MissingBackpressureException
    at rx.internal.operators...
    at rx.internal.operators...
*/
