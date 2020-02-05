// skip(), takeWhile() 등을 사용해 잘게 쪼개거나 잘라내기

Observable.range(1, 5).take(3); // [1, 2, 3]
Observable.range(1, 5).skip(3); // [4, 5]
Observable.range(1, 5).skip(5); // []


Observable.range(1, 5).takeLast(2); // [4, 5]
Observable.range(1, 5).skipLast(2); // [1, 2, 3]


Observable.range(1, 5).takeUntil(x -> x == 3);  // [1, 2, 3]
Observable.range(1, 5).takeWhile(x -> x != 3);  // [1, 2]


Observable<Integer> size = Observable
        .just('A', 'B', 'C', 'D')
        .reduce(0, (sizeSoFar, ch) -> sizeSoFar + 1);


Observable<Integer> numbers = Observable.range(1, 5);

numbers.all(x -> x != 4);    // [false]
numbers.exists(x -> x == 4); // [true]
numbers.contains(4);         // [true]
