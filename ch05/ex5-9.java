// CompletableFuture와Observable 상호 변환

// CompletableFuture를 단일 항목 Observable로 바꾸기

class Util {
    static <T> Observable<T> observe(CompletableFuture<T> future) {
	return Observable.create(subscriber -> {
	    future.whenComplete((value, exception) -> {
		if (exception != null) {
		    subscriber.onError(exception);
		} else {
		    subscriber.onNext(value);
		    subscriber.onCompleted();
		}
	    });
	});
    }
}


// 이렇게 하지 말 것!
subscriber.add(Subscriptions.create(
        () -> future.cancel(true)));


Observable<User> rxFindById(long id) {
    return Util.observe(findByIdAsync(id));
}

Observable<GeoLocation> rxLocate() {
    return Util.observe(locateAsync());
}

Observable<Ticket> rxBook(Flight flight) {
    return Util.observe(bookAsync(flight));
}


Observable<TravelAgency> agencies = agencies();
Observable<User> user = rxFindById(id);
Observable<GeoLocation> location = rxLocate();

Observable<Ticket> ticket = user
    .zipWith(location, (us, loc) ->
	agencies
	    .flatMap(agency -> agency.rxSearch(us, loc))
	    .first()
    }
    .flatMap(x -> x)
    .flatMap(this::rxBook);


import org.apache.commons.lang3.tuple.Pair;

//...
Observable<Ticket> ticket = user
        .zipWith(location, (usr, loc) -> Pair.of(usr, loc))
        .flatMap(pair -> agencies
	        .flatMap(agency -> {
		    User usr = pair.getLeft();
		    GeoLocation loc = pair.getRight();
		    return agency.rxSearch(usr, loc);
	        }))
        .first()
        .flatMap(this::rxBook);
