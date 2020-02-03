// rx.subjects.Subject

class TwitterSubject {

    private final PublishSubject<Status> subject = PublishSubject.create();

    public TwitterSubject() {
	TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
	twitterStream.addListener(new StatusListener() {
	    @Override
	    public void onStatus(Status status) {
		subject.onNext(status);
	    }

	    @Override
	    public void onException(Exception ex) {
		subject.onError(ex);
	    }

	    // 다른 콜백
	});
	twitterStream.sample();
    }

    public Observable<Status> observe() {
	return subject;
    }
}
