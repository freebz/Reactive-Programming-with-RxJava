// 블로킹 서버와 리액티브 서버 벤치마킹

wrk -t6 -c10000 -d60s --timeout 10s --latency http://server:8080


// 200 OK를 반환하는 보통 서버

// 서버 작업 시뮬레이션

public static final Observable<String> RESPONSE = Observable.just(
        "HTTP/1.1 200 OK\r\n" +
        "Content-length: 2\r\n" +
        "\r\n" +
        "OK")
    .delay(100, MILLISECONDS);


BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(1000);
executor = new ThreadPoolExecutor(100, 100, 0L, MILLISECONDS, workQueue,
        (r, ex) -> {
            ((ClientConnection) r).serviceUnavailable();
        });
