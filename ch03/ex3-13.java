// 고수준 연산자: collect(), reduce(), scan(), distinct(), groupBy()

// scan()과 reduce()로 순열 훑기

import java.util.concurrent.atomic.LongAdder;

// 잘못됐다!
Observable<Long> progress = transferFile();

LongAdder total = new LongAdder();
progress.subscribe(total:add);


Observable<Long> progress =      // [10, 14, 12, 13, 14, 16]
Observable<Long> totalProgress = /* [10, 24, 36, 49, 63, 79]

    10
    10+14=24
          24+12=36
                36+13=49
                      49+14=63
                            63+16=79
*/


Observable<Long> totalProgress = progress
    .scan((total, chunk) -> total + chunk);


Observable<BigInteger> factorials = Observable
    .range(2, 100)
    .scan(BigInteger.ONE, (big, cur) ->
        big.muliply(BigInteger.valueOf(cur)));


Observable<CashTransfer> transfers = //...;

Observable<BigDecimal> total1 = transfers
    .reduce(BigDecimal.ZERO,
        (totalSoFar, transfer) ->
	    totalSoFar.add(transfer.getAmount()));

Observable<BigDecimal> total2 = transfers
    .map(CashTransfer::getAmount)
    .reduce(BigDecimal.ZERO, BigDecimal::add);


public <R> Observable<R> reduce(
        R initialValue,
        Func2<R, T, R> accumulator) {
    return scan(initialValue, accumulator).takeLast(1);
}
