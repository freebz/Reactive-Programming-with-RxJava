// flatMap()을 비동기 체이닝 연산자처럼

List<Ticket> failures = new ArrayList<>();
for(Ticket ticket: tickets) {
    try {
	sendEmail(ticket);
    } catch (Exception e) {
	log.warn("Failed to send {}", ticket, e);
	failures.add(ticket);
    }
}


List<Pair<Ticket, Future<SmtpResponse>> tasks = tickets
    .stream()
    .map(ticket -> Pair.of(ticket, sendEmailAsync(ticket)))
    .collect(toList());

List<Ticket> failures = tasks.stream()
    .flatMap(pair -> {
        try {
	    Future<SmtpResponse> future = pair.getRight();
	    future.get(1, TimeUnit.SECONDS);
	    return Stream.empty();
	} catch (Exception e) {
	    Ticket ticket = pair.getLeft();
	    log.warn("Failed to send {}", ticket, e);
	    return Stream.of(ticket);
	}
    })
    .collect(toList9));

//----------------------------------------

private Future<SmtpResponse> sendEmailAsync(Ticket ticket) {
    return pool.submit(() -> sendEmail(ticket));
}


// 주의: 스레드 풀을 사용했지만 순차 실행이다
List<Ticket> failures = tickets
    .stream()
    .map(ticket -> Pair.of(ticket, sendEmailAsync(ticket)))
    .flatMap(pair -> {
        //...
    })
    .collect(toList());


import static rx.Observable.fromCallable;

Observable<SmtpResponse> rxSendEmail(Ticket ticket) {
    // 보편적이지 않은 동기 방식 Observable
    return fromCallable(() -> sendEmail())
}


List<Ticket> failures = Observable.from(tickets)
    .flatMap(ticket ->
	rxSendEmail(ticket)
	    .flatMap(response -> Observable.<Ticket>empty())
	    .doOnError(e -> log.warn("Failed to send {}", ticket, e))
	    .onErrorReturn(err -> ticket))
    .toList()
    .toBlocking()
    .single();


Observable
    .from(ticket)
    .flatMap(ticket ->
	rxSendEmail(ticket)
	    .ignoreElement()
	    .doOnError(e -> log.warn("Failed to send {}", ticket, e))
	    .onErrorReturn(err -> ticket)
	    .subscribeOn(Scehdulers.io()))
