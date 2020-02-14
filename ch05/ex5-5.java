// HTTP 클라이언트 코드

// RxNetty를 사용한 논블로킹 HTTP 클라이언트

Observable<ByteBuf> response = HttpClient
        .newClient("example.com", 80)
        .createGet("/")
        .flatMap(HttpClientResponse::getContent);
response
        .map(bb -> bb.toString(UTF_8))
        .subscribe(System.out::println);


Observable<URL> sources = //...

Observable<ByteBuf> packets =
    sources
    .flatMap(url -> HttpClient
	.newClient(url.getHost(), url.getPort())
	.createGet(url.getPath()))
    .flatMap(HttpClientResponse::getContent);
