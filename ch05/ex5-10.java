// Observable에서 CompletableFuture로

static <T> CompletableFuture<T> toFuture(Observable<T> observable) {
    CompletableFuture<T> promise = new CompletableFuture<>();
    observable
    	    .single()
	    .subscribe(
	            promise::complete,
		    promise::completeExceptionally
	    );
    return promise;
}


static <T> CompletableFuture<List<T>> toFutureList(Observable<T> observable)
{
    return toFuture(observable.toList());
}
