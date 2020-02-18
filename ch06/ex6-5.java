// debounce()로 낡은 이벤트 건너뛰기

Observable<BigDecimal> prices = trandingPlatform.pricesOf("NFLX");
Observable<BigDecimal> debounced = prices.debounce(100, MILLISECONDS);


prices
    .debounce(x -> {
	boolean goodPrice = x.compareTo(BigDecimal.valueOf(150)) > 0;
	return Observable
	    .empty()
	    .delay(goodPrice? 10 : 100, MILLISECONDS);
    })


Observable<BigDecimal> pricesOf(String ticket) {
    return Observable
	    .interval(50, MILLISECONDS)
	    .flatMap(this::randomDelay)
	    .map(this::randomStockPrice)
    	    .map(BigDecimal::valueOf);
}

Observable<Long> randomDelay(long x) {
    return Observable
	    .just(x)
  	    .delay((long) (Math.random() * 100), MILLISECONDS);
}

double randomStockPrice(long x) {
    return 100 + Math.random() * 10 +
	    (Math.sin(x / 100.0)) * 60.0;
}
