// Single을 만들고 사용하기

import rx.Single;

Single<String> single = Single.just("Hello, world!");
single.subscribe(System.out::println);

Single<Instant> error =
        Single.error(new RuntimeException("Opps!"));
error
        .observeOn(Schedulers.io())
        .subscribe(
	        System.out::println,
		Throwable::printStackTrace
        );


import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

Single<Response> fetch(String address) {
    return Single.create(subscriber ->
            asyncHttpClient
		    .prepareGet(address)
		    .execute(handler(subscriber)));
}

AsyncCompletionHandler handler(
SingleSubscriber<? super Response> subscriber) {
    return new AsyncCompletionHandler() {
	public Response onCompleted(Response response) {
	    subscriber.onSuccess(response);
	    return response;
	}

	public void onThrowable(Throwable t) {
	    subscriber.onError(t);
	}
    };
}


Single<String> example =
    fetch("http://www.example.com")
        .flatMap(this::body);

String b = example.toBlocking().value();

//...

Single<String> body(Response response) {
    return Single.create(subscriber -> {
	try {
	    subscriber.onSuccess(response.getResponseBody());
	} catch (IOException e) {
	    subscriber.onError(e);
	}
    });
}

// body()와 같은 기능
Single<String> body2(Response response) {
    return Single.fromCallable(() ->
        response.getResponseBody());
}
