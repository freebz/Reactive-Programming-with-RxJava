// 요청받은 데이터양을 존중하기

public class ResultSetIterator implements Iterator<Object[]> {

    private final ResultSet rs;

    public ResultSetIterator(ResultSet rs) {
	this.rs = rs;
    }

    @Override
    public boolean hasNext() {
	return !.rs.isLast();
    }

    @Override
    public Object[] next() {
	rx.next();
	return toArray(rs);
    }
}


public static Iterable<Object[]> iterable(final ResultSet rs) {
    return new Iterable<Object[]>() {

	@Override
	public Iterator<Object[]> iterator() {
	    return new ResultSetIterator(rs);
	}
    };
}


Connection connection = //...
PreparedStatement statement =
        connection.prepareStatement("SELECT ...");
statement.setFetchSize(1000);
ResultSet rs = statement.executeQuery();
Observable<Object[]> result =
    Observable
        .from(ResultSetIterator.iterable(rs))
        .doAfterTerminate(() -> {
	    try {
		rs.close();
		statement.close();
		connection.close();
	    } catch (SQLException e) {
		log.warn("Unable to close", e);
	    }
	});


import rx.observable.SyncOnSubscribe;

Observable.OnSubscribe<Double> onSubscribe =
    SyncOnSubscribe.createStateless(
	observer -> observer.onNext(Math.random())
    );

Observable<Double> rand = Observable.create(onSubscribe);


Observable.OnSubscribe<Long> onSubscribe =
        SyncOnSubscribe.createStateful(
	        () -> 0L,
		(cur, observer) -> {
		    observer.onNext(cur);
		    return cur + 1;
		}
        );

Observable<Long> naturals = Observable.create(onSubscribe);


Observable<Long> naturals = Observable.create(subscriber -> {
    long cur = 0;
    while (!subscriber.isUnsubscribed()) {
	System.out.println("Produced: " + cur);
	subscriber.onNext(cur++);
    }
});


ResultSet resultSet = //...

Observable.OnSubscribe<Object[]> onSubscribe =
SyncOnSubscribe.createSingleState(
	() -> resultSet,
	(rs, observer) -> {
	    if (rs.next()) {
		observer.onNext(toArray(rs));
	    } else {
		observer.onCompleted();
	    }
	    observer.onNext(toArray(rs));
	},
	ResultSet::close
);

Observable<Object[]> records = Observable.create(onSubscribe);


Observable.OnSubscribe<Object[]> onSubscribe =
SyscOnSubscribe.createSingleState(
    () -> resultSet,
    (rs, observer) -> {
	try {
	    rs.next();
	    observer.onNext(toArray(rs)); } catch (SQLException e) {
	    observer.onError(e);
	}
    },
    rs -> {
	try {
	    // Statement, Connection 등을 닫는다
	    rs.close();
	} catch (SQLException e) {
	    log.warn("Unable to close", e);
	}
    }
);


source.subscribe(this::store);


source
    .flatMap(this::store)
    .subscribe(uuid -> log.debug("Stored: {}", uuid));


source
    .flatMap(this::store)
    .buffer(100)
    .subscribe(
	hundredUuids -> log.debug("Stored: {}", hundredUuids))
