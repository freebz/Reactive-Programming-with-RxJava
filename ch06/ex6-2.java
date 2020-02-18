// 이벤트를 리스트에 버퍼링하기

Observable
    .range(1, 7) // 1, 2, 3, ... 7
    .buffer(3)
    .subscribe((List<Integer> list) -> {
	    System.out.println(list);
	}
    );


/*
[1, 2, 3]
[4, 5, 6]
[7]
*/


interface Repository {
    void store(Record record);
    void storeAll(List<Record> records);
}

//...

Observable<Record> events = //...

events
        .subscribe(repository::store);
// vs.
events
        .buffer(10)
        .subscribe(repository::storeAll);


Observable
        .range(1, 7)
        .buffer(3, 1)
        .subscribe(System.out::println);


/*
[1, 2, 3]
[2, 3, 4]
[3, 4, 5]
[4, 5, 6]
[5, 6, 7]
[6, 7]
[7]
*/


import java.util.Random;
import java.util.stream.Collectors;

//...

Random random = new Random();
Observable
        .defer(() -> just(random.nextGaussian()))
        .repeat(1000)
        .buffer(100, 1)
        .map(this::averageOfList)
        .subscribe(System.out::println);

//...

private double averageOfList(List<Double> list) {
    return list
   	    .stream()
	    .collect(Collectors.averagingDouble(x -> x));
}


Observable<List<Integer>> odd = Observable
        .range(1, 7)
        .buffer(1, 2);
odd.subscribe(System.out::println);


/*
[1]
[3]
[5]
[7]
*/


Observable<Integer> odd = Observable
        .range(1, 7)
        .buffer(1, 2)
        .flatMapIterable(list -> list);
