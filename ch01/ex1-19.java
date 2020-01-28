// Completable

Completable c = writeToDatabase("data");


Observable<Void> c = writeToDatabase("data");


static Completable writeToDatabase(Object data) {
    return Completable.create(s -> {
	doAsyncWrite(data,
	    // 성공적인 완료 시 콜백
	    () -> s.onCompleted(),
	    // Throwable을 포함하는 실패 시 콜백
	    error -> s.onError(error));
    });
}
