// groupBy()로 요청을 일괄 처리하기

import org.apache.commons.lang3.tuple.Pair;

Observable<BigDecimal> totalPrice = Observable
    .just("bread", "butter", "egg", "milk", "tomato",
      "cheese", "tomato", "egg", "egg")
    .groupBy(prod -> prod)
    .flatMap(grouped -> grouped
        .count()
	.map(quantity -> {
	    String productName = grouped.getKey();
	    return Pair.of(productName, quantity);
	}))
    .flatMap(order -> store
	.purchase(order.getKey(), order.getValue())
	.subscribeOn(schedulerA))
    .reduce(BigDecimal::add)
    .single();


/*
164  | Sched-A-0 | Purchasing 1 bread
165  | Sched-A-1 | Purchasing 1 butter
166  | Sched-A-2 | Purchasing 3 egg
166  | Sched-A-3 | Purchasing 1 milk
166  | Sched-A-4 | Purchasing 2 tomato
166  | Sched-A-5 | Purchasing 1 cheese
1151 | Sched-A-0 | Done 1 bread
1178 | Sched-A-1 | Done 1 butter
1180 | Sched-A-5 | Done 1 cheese
1183 | Sched-A-3 | Done 1 milk
1253 | Sched-A-4 | Done 2 tomato
1354 | Sched-A-2 | Done 3 egg
*/
