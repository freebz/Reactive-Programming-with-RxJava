// Observer<T>로 모든 알림 잡아내기

Observer<Tweet> observer = new Observer<Tweet>() {
    @Override
    public void onNext(Tweet tweet) {
	System.out.println(tweet);
    }

    @Override
    public void onError(Throwable e) {
	e.printStackTrace();
    }

    @Override
    public void onCompleted() {
	noMOre();
    }
};

//...

tweets.subscribe(observer);
