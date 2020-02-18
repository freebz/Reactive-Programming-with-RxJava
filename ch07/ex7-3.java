// onErrorReturn()을 사용하여 오류를 고정된 결과로 바꾸기

Observable<Income> income = person
    .flatMap(this::determineIncome)
    .onErrorReturn(error -> Income.no())

//...

private Observable<Income> determineIncome(Person person) {
    return Observable.error(new RuntimeException("Foo"));
}

class Income {
    static Income no() {
	return new Income(0);
    }
}


try {
    return determineIncome(Person person)
} catch(Exception e) {
    return Income.no();
}
