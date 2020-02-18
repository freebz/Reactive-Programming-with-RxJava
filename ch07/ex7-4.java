// onErrorResumeNext()로 오류 대비책을 느긋하게 계산하기

Observable<Person> person = //...
Observable<Income> income = person
    .flatMap(this::determineIncome)
    .onErrorResumeNext(person.flatMap(this::guessIncome));

//...

private Observable<Income> guessIncome(Person person) {
    //...
}


Observable<Income> income = person
    .flatMap(this::determineIncome)
    .flatMap(
	Observable::just,
	th -> Observable.empty(),
	Observable::empty)
    .concatWith(person.flatMap(this::guessIncome))
    .first();


Observable<Income> income = person
    .flatMap(this::determineIncome)
    .flatMap(
	Observable.just,
	th -> person.flatMap(this::guessIncome),
	Observable::empty);


Observable<Income> income = person
    .flatMap(this::determineIncome)
    .onErrorResumeNext(th -> {
	if (th instanceof NullPointerException) {
	    return Observable.error(th);
	} else {
	    return person.flatMap(this::guessIncome);
	}
    });
