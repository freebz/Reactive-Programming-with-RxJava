// Scheduler의 다른 사용법

Observable
        .just('A', 'B')
        .delay(1, SECONDS, schedulerA)
        .subscribe(this::log);
