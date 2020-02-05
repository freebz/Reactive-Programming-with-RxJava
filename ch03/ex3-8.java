// 여러 개의 Observable

// merge()로 여러 Observable을 하나처럼 다루기

Observable<LicensePlate> fastAlgo(CarPhoto photo) {
    // 빠르지만 낮은 품질
}

Observable<LicensePlate> preciseAlgo(CarPhoto photo) {
    // 정확하지만 비용이 높을 수 있음
}

Observable<LicensePlate> experimentalAlgo(CarPhoto photo) {
    // 예측할 수 없지만, 어째든 실행됨
}


Observable<LicensePlate> all = Observable.merge(
        preciseAlgo(photo),
        fastAlgo(photo),
        experimentalAlgo(photo)
);
