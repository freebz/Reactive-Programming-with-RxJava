// Single

public static Single<String> getDataA() {
    return Single.<String> create(o -> {
	o.onSuccess("DataA");
    }).subscribe(Schedulers.io());
}

public static Single<String> getDataB() {
    return Single.just("DataB").subscribe(Schedulers.io());
}


// a와 b를 2개의 값을 가진 Observable 스트림으로 병합
Observable<String> a_merge_b = getDataA().mergeWith(getDataB());


// Observable<String> o1 = getDataAsObservable(1);
// Observable<String> o2 = getDataAsObservable(2);

Single<String> s1 = getDataAsSingle(1);
Single<String> s2 = getDataAsSingle(2);

// o3는 s1과 s2의 스트림이며 각 항목은 대기하지 않고 방출한다
Observable<String> o3 = Single.merge(s1, s2);
