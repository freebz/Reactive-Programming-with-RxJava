// Subscription과 Subscriber<T>로 리스너 제어하기

Subscription subscription = tweets.subscribe(System.out::println);

//...
subscription.unsubscribe();


Subscriber<Tweet> subscriber = new Subscriber<Tweet>() {
    @Override
    public void onNext(Tweet tweet) {
	if (tweet.getText().contains("Java")) {
	    unsubscribe();
	}
    }
    
    @Override
    public void onCompleted() {}
    
    @Override
    public void onError(Throwable e) {
	e.printStackTrace();
    }
};
tweets.subscribe(subscriber);
