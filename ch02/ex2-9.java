// 사례: 콜백 API를 Observable 스트림으로

while(true) {
    Event event = blockWaitingForNewEvent();
    doSomethingWith(event);
}


button.setOnClickListener(view -> {
    MyApi.asyncRequest(response -> {
	Thread thread = new Thread(() -> {
	    int year = datePicker.getYear();
	    runOnUiThread(() -> {
		button.setEnabled(false);
		button.setText("" + year);
	    });
	});
	thread.setDeamon(true);
	thread.start();
    });
});


import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
twitterStream.addListener(new twitter4j.StatusListener() {
    @Override
    public void onStatus(Status status) {
	log.info("Status: {}", status);
    }

    @Override
    public void onException(Exception ex) {
	log.error("Error callback", ex);
    }

    // 다를 콜백들
});
twitterStream.sample();
TimeUnit.SECONDS.sleep(10);
twitterStream.shutdown();


void consume(
            Consumer<Status> onStatus,
            Consumer<Exception> onException) {
    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
    twetterStream.addListener(new StatusListener() {
	@Override
	public void onStatus(Status status) {
	    onStatus.accept(status);
	}

	@Override
	public void onException(Exception ex) {
	    onException.accept(ex);
	}

	// 다른 콜백들
    });
    twitterStream.sample();
}


consume(
    status -> log.info("Status: {}", status),
    ex     -> log.error("Error callback", ex)
);


Observable<Status> observe() {
    return Observable.create(subscriber -> {
	TwitterStream twitterStream =
	    new TwitterStreamFactory().getInstance();
	twitterStream.addListener(new StatusListener() {
	    @Override
	    public void onStatus(Status status) {
		subscriber.onNext(status);
	    }

	    @Override
	    public void onException(Exception ex) {
		subscriber.onError(ex);
	    }

	    // 다른 콜백들
	});
	subscriber.add(Subscriptions.create(twitterStream::shutdown));
    });
}


observe().subscribe(
        status -> log.info("Status: {}", status),
	ex -> log.error("Error callback", ex)
);


@Override
public void onStatus(Status status) {
    if (subscriber.isUnsubscribed()) {
	twitterStream.shutdown();
    } else {
	subscriber.onNext(status);
    }
}

@Override
public void onException(Exception ex) {
    if (subscriber.isUnsubscribed()) {
	twitterStream.shutdown();
    } else {
	subscriber.onError(ex);
    }
}


twitterStream.addListener(new StatusListener() {
    // 콜백...
});
twitterStream.sample();

subscriber.add(Subscriptions.create(twitterStream::shutdown));
