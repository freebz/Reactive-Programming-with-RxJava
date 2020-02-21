// 모니터링과 대시보드

import com.netflix.hystrix.metric.HystrixCommandCompletion;
import com.netflix.hystrix.metric.HystrixCommandCompletionStream;

Observable<HystrixCommandCompletion> stats =
    HystrixCommandCompletionStream
    .getInstance(HystrixCommandKey.Factory.asKey("FetchRating"))
    .observe();


import static com.netflix.hystrix.HystrixEventType.FAILURE;

HystrixCommandCompletionStream
        .getInstance(HystrixCommandKey.Factory.asKey("FetchRating"))
        .observe()
        .filter(e -> e.getEventCounts().getCount(FAILURE) > 0)
        .window(1, TimeUnit.SECONDS)
        .flatMap(Observable::count)
        .subscribe(x -> log.info("{} failures/s", x));


import
    com.netflix.hystrix.contrib.metrics.eventstream.
        HystrixMetricsStreamServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

//...

ServletContextHandler context = new ServletContextHandler(NO_SESSIONS);
HystrixMetricsStreamServlet servlet = new HystrixMetricsStreamServlet();
context.addServlet(new ServletHolder(servlet), "/hystrix.stream");
Server server = new Server(8080);
server.setHandler(context);
server.start();


curl -v localhost:8080/hystrix.stream
> GET /hystrix.stream HTTP/1.1
...
< HTTP/1.1 200 OK
< Content-Type: text/event-stream;charset=UTF-8

ping:

data: {
    "currentConcurrentExecuttionCount": 2,
    "errorCount": 0,
    "errorPercentage": 0,
    "group": "Books",
    "isCircuitBreakerOpen": false,
    "latencyExecute": {/* ... */},
    "latencyTotal": {"0":18, "25":80, "50":98, "75":120, "90":138,
		     "95":146, "99":159, "99.5":159, "100":167},
    "latencyTotal_mean": 0,
    "name": "FetchRating",
    "propertyValue_circuitBreakerErrorThresholdPercentage": 50,
    "propertyValue_circuitBreakerSleepWindowInMilliseconds": 5000,
    "propertyValue_executionIsolationSemaphoreMaxConcurrentRequests": 10,
    "propertyValue_executionTimeoutInMilliseconds": 1000,
    "requestCount": 334
    ...
}

data: { ...
