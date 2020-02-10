// 주기적으로 변경 사항을 폴링하기

Observable
        .interval(10, TimeUnit.MILLISECONDS)
        .map(x -> getOrderBookLength())
        .distinctUntilChanged()


Observable<Item> observeNewItems() {
    return Observable
	.interval(1, TimeUnit.SECONDS)
	.flatMapIterable(x -> query())
	.distinct();
}

List<Item> query() {
    // 파일 시스템의 디렉터리나
    // 데이터베이스 테이블의 스냅샷을 취한다
}
