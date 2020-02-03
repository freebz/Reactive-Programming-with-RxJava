// ConnectableObservable의 생명 주기

Observable<Status> tweets = //...
return tweets
    .doOnNext(this::saveStatus);


Observable<Status> tweets = //...
tweets
    .doOnNext(this::saveSttus)
    .subscribe();


ConnectableObservable<Status> published = tweets.publish();
published.connect();


import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import rx.Observable;
import rx.observables.ConnectableObservable;

@Configuration
class Config implements ApplicationListener<ContextRefreshedEvent> {

    private final ConnectableObservable<Statue> observable =
	Observable.<Status>create(subscriber -> {
	    log.info("Starting");
	    //...
        }).publish();

    @Bean
    public Observable<Status> observable() {
	return observable;
    }

    @Oberride
    public void onApplicationEvent(ContextRefreshedEvent event) {
	log.info("Connecting");
	observable.connect();
    }
}

@Component
class Foo {

    @Autowired
    public Foo(Observable<Status> tweets) {
	tweets.subscribe(status -> {
	    log.info(status.getText());
	});
	log.info("Subscribed");
    }
}

@Component
class Bar {

    @Autowired
    public Bar(Observable<Status> tweets) {
	tweets.subscribe(status -> {
	    log.info(status.getText());
	});
	log.info("Subscribed");
    }
}


/*
[Foo   ] Subscribed
[Bar   ] Subscribed
[Config] Connecting
[Config] Starting
[Foo   ] Msg 1
[Bar   ] Msg 1
[Foo   ] Msg 2
[Bar   ] Msg 2
*/


/*
[Config] Starting
[Foo   ] Subscribed
[Foo   ] Msg 1
[Config] Starting
[Bar   ] Subscribed
[Foo   ] Msg 2
[Bar   ] Msg 2
*/
