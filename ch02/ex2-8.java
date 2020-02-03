// 타이밍: timer()와 interval()

Observable
        .timer(1, TimeUnit.SECONDS)
        .subscribe((Long zero) -> log(zero));


Observable
        .interval(1_000_000 / 60, MICROSECONDS)
        .subscribe((Long i) -> log(i));
