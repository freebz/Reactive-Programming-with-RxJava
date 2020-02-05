// flatMap()으로 마무리하기

import static rx.Observable.empty;
import static rx.Observable.just;

numbers.map(x -> x * 2);
numbers.filter(x -> x != 10);

// 같은 구현
numbers.flatMap(x -> just(x * 2));
numbers.flatMap(x -> (x != 10) ? just(x) : empty());


Observable<CarPhoto> cars() {
    //...
}

Observable<LicensePlate> recognize(CarPhoto photo) {
    //...
}


Observable<CarPhoto> cars = cars();

Observable<Observable<LicensePlate>> plates =
        cars.map(this::recognize);

Observable<LicensePlate> plate2 =
        cars.flatMap(this::recognize);


Observable<Custoer> customers = //...
Observable<Order> orders = customers
        .flatMap(customer ->
	    Observable.from(customer.getOrders()));


Observable<Order> orders = customers
        .map(Customer::getOrders)
        .flatMap(Observable::from);


Observable<Order> orders = customers
        .flatMapIterable(Customer::getOrders);


<R> Observable<R> flatMap(
        Func1<T, Observable<R> onNext,
        Func1<Throwable, Observable<R>> onError,
        Func0<Observable<R>> onCompleted)
    

void store(UUID id) {
    upload(id).subscribe(
	    bytes -> {}, // 무시
	    e -> log.error("Error", e),
	    () -> rate(id)
    );
}

Observable<Long> upload(UUID id) {
    //...
}

Observable<Rating> rate(UUID id) {
    //...
}


upload(id)
        .flatMap(
	        bytes -> Observable.empty(),
		e -> Observable.error(e),
		() -> rate(id)
	);


import static rx.Observable.empty;
import static rx.Observable.just;

Observable<Sound> toMorseCode(char ch) {
    switch(ch) {
    case 'a': return just(DI, DAH);
    case 'b': return just(DAH, DI, DI, DI);
    case 'c': return just(DAH, DI, DAH, DI);
    //...
    case 'p': return jsut(DI, DAH, DAH, DI);
    case 'r': return just(DI, DAH, DI);
    case 's': return just(DI, DI, DI);
    case 't': return just(DHA);
    //...
    default:
	return empty();
    }
}

enum Sound { DI, DAH }

//...

just('S', 'p', 'a', 'r', 't', 'a')
    .map(Character::toLowerCase)
    .flatMap(this::toMorseCode)
