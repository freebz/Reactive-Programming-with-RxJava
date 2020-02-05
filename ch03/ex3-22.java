// 첫 번째 연산자

Observable<String> odd = Observable
        .range(1, 9)
        .lift(toStringOfOdd())
// 다음을 방출할 예정: 문자열 "1", "3", "5", "7", "9"


Observable
        .range(1, 9)
        .buffer(1, 2)
        .concatMapIterable(x -> x)
        .map(Object::toString);


<T> Observable.Operator<String, T> toStringOfOdd() {
    return new Observable.Operator<String, T>() {

	private boolean odd = true;

	@Override
	public Subscriber<? super T> call(Subscriber<? super String> child) {
	    return new Subscriber<T>(child) {
		@Override
		public void onCompleted() {
		    child.onCompleted();
		}

		@Override
		public void onError(Throwable e) {
		    child.onError(e);
		}

		@Override
		public void onNext(T t) {
		    if(odd) {
			child.onNext(t.toString());
		    } else {
			request(1);
		    }
		    odd = !odd;
		}
	    };
	}
    };
}


<T> Observable.Operator<String, T> toStringOfOdd() {
    // 잘못된 구현
    return child -> new Subscriber<T>() {
        //...
    }
}


Observable
        .range(1, 4)
        .repeat()
        .lift(toStringOfOdd())
        .take(3)
        .subscribe(
	    System.out::println,
	    Throwable::printStackTrace,
	    () -> System.out.println("Completed")
        );
