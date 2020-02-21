// 히스트릭스로 장애 관리하기

// 히스트릭스 첫걸음

import org.apahce.commons.io.IOUtils;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

class BlockingCmd extends HystrixCommand<String> {

    public BlockingCmd() {
	super(HystrixCommandGroupKey.Factory.asKey("SomeGroup"));
    }

    @Override
    protected String run() throws IOException {
	final URL url = new URL("http://www.example.com");
	try (InputStream input = url.openStream()) {
	    return IOUtils.toString(input, StandardCharsets.UTF_8);
	}
    }

}


String string         = new BlockingCmd().execute();
Future<String> future = new BlockingCmd().queue();


Observable<String> eager = new BlockingCmd().observe();
Observeble<String> lazy = new BlockingCmd().toObservable();


Observable<String> retried = new BlockingCmd()
    .toObservable()
    .doOnError(ex -> log.warn("Error", ex))
    .retryWhen(ex -> ex.delay(500, MILLISECONDS))
    .timeout(3, SECONDS);
