// Observable 알림 구독

Observable<Tweet> tweets = //...

tweets.subscribe((Tweet wteet) ->
    System.out.println(tweet));


tweets.subscribe(
        (Tweet tweet) -> { System.out.println(tweet); },
        (Throwable t) -> { t.printStackTrace(); }
);


tweets.subscribe(
        (Tweet tweet) -> { System.out.println(tweet); },
        (Throwable t) -> { t.printStackTrace(); },
        () -> {this.noMore();}
);


tweets.subscribe(
    System.out::println,
    Throable::printStackTrace,
    this::noMore);
