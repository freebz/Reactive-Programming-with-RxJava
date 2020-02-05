// withLatestFrom() 연산자

Observable<String> fast = interval(10, MILLISECONDS).map(x -> "F" + x);
Observable<String> slow = interval(17, MILLISECONDS).map(x -> "S" + x);
slow
    .withLatestFrom(fast, (s, f) -> s + ":" + f)
    .forEach(System.out::println);


/*
S0:F1
S1:F2
S2:F4
S3:F5
S4:F7
S5:F9
S6:F11
...
*/


Observalbe<String> fast = interval(10, MILLISECONDS)
        .map(x -> "F" + x)
        .delay(100, MILLISECONDS)
        .startWith("FX");
Observalbe<String> slow = interval(17, MILLISECONDS).map(x -> "S" + x);
slow
        .withLatestFrom(fast, (s, f) -> s + ":" + f)
        .forEach(System.out::println);


/*
S0:FX
S1:FX
S2:FX
S3:FX
S4:FX
S5:FX
S6:F1
S7:F3
S8:F4
S9:F6
...
*/


Observable
        .just(1, 2)
        .startWith(0)
        .subscribe(System.out::println);
