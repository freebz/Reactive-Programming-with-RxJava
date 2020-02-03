// 수동으로 Subscriber 관리하기

// 이렇게 하지 말자. 무척 다루기 어렵고 오류가 넘쳐난다.
class LazyTwitterObservable {
    private final Set<Subscriber<? super Status>> subscribers =
	new CopyOnWriteArraySet<>();

    private final TwitterStream twitterStream;

    public LazyTwitterObservable() {
	this.twitterStream = new TwitterStreamFActory().getInstance();
	this.twitterStream.addListener(new StatusListener() {
	    @Override
	    public void onStatus(Status status) {
		subscribers.forEach(s -> s.onNext(status));
	    }

	    @Override
	    public void onException(Exception ex) {
		subscribers.forEach(s -> s.onError(ex));
	    }

	    // 다른 콜백
	});
    }

    private final Observable<Status> observable = Observable.create(
	subscriber -> {
	    register(subscriber);
	    subscriber.add(Subscriptions.create(() ->
    	        this.deregister(subscriber)));
	});

    Observable<Status> observe() {
	return observable;
    }

    private synchronized void register(Subscriber<? super Status> subscriber) {
	if (subscribers.isEmpty()) {
	    subscribers.add(subscriber);
	    twitterStream.sample();
	} else {
	    subscribers.add(subscriber);
	}
    }

    private synchronized void deregister(Subscriber<? super Status> subscriber) {
	subscribers.remove(subscriber);
	if (subscribers.isEmpty()) {
	    twitterStream.shutdown();
	}
    }
}
