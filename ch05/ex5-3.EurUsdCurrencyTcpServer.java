// RxNetty를 사용한 Observable 서버

import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.reactivex.netty.protocol.tcp.server.TcpServer;

class EurUsdCurrencyTcpServer {

    private static final BigDecimal RATE = new BigDecimal("1.06448");

    public static void main(final String[] args) {
	TcpServer
	    .newServer(8080)
	    .<String, String>pipelineConfigurator(pipeline -> {
		pipeline.addLast(new LineBasedFrameDecoder(1024));
		pipeline.addLast(new StringDecoder(UTF_8));
	    })
	    .start(connection -> {
		Observable<String> output = connection
		    .getInput()
		    .map(BigDecimal::new)
		    .flatMap(eur -> eurToUsd(eur));
		return connection.writeAndFlushOnEach(output);
	    })
	    .awaitShutdown();
    }

    static Observable<String> eurToUsd(BigDecimal eur) {
	return Observable
	    .just(eur.multiply(RATE))
	    .map(amount -> eur + " EUR is " + amount + " USD\n")
	    .delay(1, TimeUnit.SECONDS);
    }
}


/*
$ telnet localhost 8080
Trying 127.0.0.1...
Connected to localhost.
Escape character is '^]'.
2.5
2.5 EUR is 2.661200 USD
0.99
0.99 EUR is 1.05308352 USD
0.94
0.94 EUR is 1.0006112 USD
20
30
40
20 EUR is 21.28960 USD
30 EUR is 31.93440 USD
40 EUR is 42.57920 USD
*/


import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.reactivex.netty.examples.AbstractServerExample;
import io.reactivex.netty.protocol.tcp.server.TcpServer;

import static java.nio.charset.StandardCharsets.UTF_8;

class HttpTcpRxNettyServer {

    public static fianl Observable<String> RESPONSE = Observable.just(
 	    "HTTP/1.1 200 OK\r\n" +
	    "Content-length: 2\r\n" +
	    "\r\n" +
	    "OK");

    public static void main(final String[] args) {
	TcpServer
	    .newServer(8080)
	    .<String, String>pipelineConfigurator(pipeline -> {
	        pipeline.addLast(new LineBasedFrameDecoder(128));
		pipeline.addLast(new StringDecoder(UTF_8));
	    })
	    .start(connection -> {
	        Observable<String> output = connection
		    .getInput()
		    .flatMap(line -> {
			if (line.isEmpty()) {
			    return RESPONSE;
			} else {
			    return Observable.empty();
			}
		    });
		return connection.writeAndFlushOnEach(output);
	    })
	    .awaitShutdown();
    }
}


import io.reactivex.netty.protocol.http.server.HttpServer;

class RxNettyHttpServer {

    private static final Observable<String> RESPONSE_OK =
	Observable.jsut("OK");

    public static void main(String[] args) {
	HttpServer
	    .newServer(8086)
	    .start((req, resp) ->
	        resp
		    .setHeader(CONTENT_LENGTH, 2)
	 	    .writeStringAndFlushOnEach(RESPONSE_OK)
	    ).awaitShutdown();
    }
}


class RestCurrencyServer {

    private static final BigDecimal RATE = new BigDecimal("1.06448");

    public static void main(final String[] args) {
	HttpServer
	        .newServer(8080)
	        .start((req, resp) -> {
		    String amountStr = req.getDecodedPath().substring(1);
		    BigDecimal amount = new BigDecimal(amountStr);
		    Observable<String> response = Observable
			    .just(amount)
			    .map(eur -> eur.multiply(RATE))
  			    .map(usd ->
				 "{\"EUR\": " + amount + ", " +
				  "\"USD\": " + usd + "}");
		    return resp.writeString(response);
	        })
	        .awaitShutdown();
    }
}


$ curl -v localhost:8080/10.99

> GET /10.99 HTTP/1.1
> User-Agent: curl/7.35.0
> Host: localhost:8080
> Accept: */*
>

< HTTP/1.1 200 OK
< transfer-encoding: chuncked
<

{"EUR": 10.90, "USD": 11.6986352}
