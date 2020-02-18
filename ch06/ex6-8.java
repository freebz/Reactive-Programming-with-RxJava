// 내장 배압

Observable
    .range(1, 10)
    .subscribe(new Subscriber<Integer>() {

        @Override
	public void onStart() {
	    request(3);
	}

	// onNext, onCompleted, onError가 이어진다...
    });


.subscribe(new Subscriber<Integer>() {

    {{
	request(3);
    }}

    // onNext, onCompleted, onError가 이어진다...
});


Observable
    .range(1, 10)
    .subscribe(new Subscriber<Integer> () {

	@Override
	public void onStart() {
	    request(1);
	}

	@Override
	public void onNext(Integer integer) {
	    request(1);
	    log.info("Next {}", integer);
	}

	// onCompleted, onError...
    });
