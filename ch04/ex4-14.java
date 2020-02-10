// subscribeOn()의 동시성과 동작 방식

log("Starting");
Observable<String> obs = simple();
log("Created");
obs
        .subscribeOn(schedulerA)
        // 수많은 연산자
        .subscribeOn(schedulerB)
        .subscribe(
	        x -> log("Got " + x),
		Throwable::printStackTrace,
		() -> log("Completed")
        );
log("Exiting");


/*
17  | main  | Starting
73  | main  | Created
83  | main  | Exiting
84  | Sched-A-0 | Subscribed
84  | Sched-A-0 | Got A
84  | Sched-A-0 | Got B
84  | Sched-A-0 | Completed
*/


log("Starting");
fianl Observable<String> obs = simple();
log("Created");
obs
        .doOnNext(this::log)
        .map(x -> x + '1')
        .doOnNext(this::log)
        .map(x -> x + '2')
        .subscribeOn(schedulerA)
        .doOnNext(this::log)
        .subscribe(
	        x -> log("Got " + x),
		Throwable::printStackTrace,
		() -> log("Completed")
	);
log("Exiting");


/*
20  | main  | String
104 | main  | Created
123 | main  | Exiting
124 | Sched-A-0 | Subscribed
124 | Sched-A-0 | A
124 | Sched-A-0 | A1
124 | Sched-A-0 | A12
124 | Sched-A-0 | Got A12
124 | Sched-A-0 | B
124 | Sched-A-0 | B1
124 | Sched-A-0 | B12
125 | Sched-A-0 | Got B12
*/


class RxGroceries {

    Observable<BigDecimal> purchase(String productName, int quantity) {
	return Observable.fromCallable(() ->
            doPurchase(productName, quantity));
    }

    BigDecimal doPurchase(String productName, int quantity) {
	log("Purchasing " + quantity + " " + productName);
	// 실제 로직은 여기에
	log("Done " + quantity + " " + productName);
	return priceForProduct;
    }
}


Observable<BigDecimal> totalPrice = Observable
        .just("bread", "butter", "milk", "tomato", "cheese")
        .subscribeOn(schedulerA) // 잘못됐다!!!
        .map(prod -> rxGroceries.doPurchase(prod, 1))
        .reduce(BigDecimal::add)
        .single();


/*
144  | Sched-A-0 | Purchasing 1 bred
1144 | Sched-A-0 | Done 1 bread
1146 | Sched-A-0 | Purchasing 1 butter
2146 | Sched-A-0 | Done 1 butter
2146 | Sched-A-0 | Purchasing 1 milk
3147 | Sched-A-0 | Done 1 milk
3147 | Sched-A-0 | Purchasing 1 tomato
4147 | Sched-A-0 | Done 1 tomato
4147 | Sched-A-0 | Purchasing 1 cheese
5148 | Sched-A-0 | Done 1 cheese
*/


Observable<BigDecimal> totalPrice = Observable
        .just("bread", "butter", "milk", "tomato", "cheese")
        .subscribeOn(schedulerA)
        .flatMap(prod -> rxGroceries.doPurchase(prod, 1))
        .reduce(BigDecimal::add)
        .single();


/*
동일
*/


Observable<BigDecimal> totalPrice = Observable
        .just("bread", "butter", "milk", "tomato", "cheese")
        .faltMap(prod ->
	        rxGroceries
		        .purchase(prod, 1)
		        .subscribeOn(schedulerA))
        .reduce(BigDecimal::add)
        .single();


/*
113  | Sched-A-1 | Purchasing 1 butter
114  | Sched-A-0 | Purchasing 1 bread
125  | Sched-A-2 | Purchasing 1 milk
125  | Sched-A-3 | Purchasing 1 tomato
126  | Sched-A-4 | Purchasing 1 cheese
1126 | Sched-A-2 | Done 1 milk
1126 | Sched-A-0 | Done 1 bread
1126 | Sched-A-1 | Done 1 butter
1128 | Sched-A-3 | Done 1 tomato
1128 | Sched-A-4 | Done 1 cheese
*/
