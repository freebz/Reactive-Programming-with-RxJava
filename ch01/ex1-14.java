// 쌍대성(duality)

// 75개의 문자열로 이루어진
// Iterable<String>을 Stream<String>으로
getDataFromLocalMemorySynchronously()
    .skip(10)
    .limit(5)
    .map(s -> s + "_transformed")
    .forEach(System.out::println)


// 75개 문자열을
// 방출하는 Observable<String>
getDataFromNetworkAsynchronously()
    .skip(10)
    .take(5)
    .map(s -> s + "_transformed")
    .subscribe(System.out::println)
