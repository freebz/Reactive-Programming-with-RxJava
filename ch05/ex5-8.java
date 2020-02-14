// CompletableFuture와 스트림

// 간략한 CompletableFuture 소개

User findById(long id) {
    //...
}

GeoLocation locate() {
    //...
}

Ticket book(Flight flight) {
    //...
}

interface TravelAgency {
    Flight search(User user, GeoLocation location);
}


ExecutorService pool = Executors.newFixedThreadPool(10);
List<TravelAgency> agencies = //...

User user = findById(id);
GeoLocation location = locate();
ExecutorCompletionService<Flight> ecs =
    new ExecutorCompletionService<>(pool);
agencies.forEach(agency ->
    ecs.submit(() ->
        agency.search(user, location)));
Future<Flight> firstFlight = ecs.pool(5, SECONDS);
Flight flight = firstFlight.get();
book(flight);


CompletableFuture<User> findByIdAsync(long id) {
    return CompletableFuture.supplyAsync(() -> findById(id));
}

CompletableFuture<GeoLocation> locateAsync() {
    return CompletableFuture.supplyAsync(this::locate);
}

CompletableFuture<Ticket> bookAsync(Flight flight) {
    return CompletableFuture.supplyAsync(() -> book(flight));
}

@Override
public CompletableFuture<Flight> searchAsync(
User user, GeoLocation location) {
    return CompletableFuture.supplyAsync(() -> search(user, location));
}


import static java.util.function.Function.identity;

List<TravelAgency> agencies = //...
CompletableFuture<User> user = findByIdAsync(id);
CompletableFuture<GeoLocation> location = locateAsync();

CompletableFuture<Ticket> ticketFuture = user
    .thenCombine(location, (User us, GeoLocation loc) -> agencies
        .stream()
	.map(agency -> agency.searchAsync(us, loc))
	.reduce((f1, f2) ->
	    f1.applyToEither(f2, identity())
	)
        .get()
    )
    .thenCompose(identity())
    .thenCompose(this::bookAsync);


CompletableFuture<Long> timeFuture = //...
CompletableFuture<ZoneId> zoneFuture = //...

CompletableFuture<Instant> instantFuture = timeFuture
        .thenApply(time -> Instant.ofEpocMilli(time));

CompletableFuture<ZonedDateTime> zdtFuture = instantFuture
        .thenCombine(zoneFuture, (instant, zoneId) ->
	        ZonedDateTime.ofInstant(instant, zoneId));


List<TravelAgency> agencies = //...

agencies
    .stream()
    .map(agency -> agency.searchAsync(us, loc))
    .reduce((f1, f2) ->
            f1.applyToEither(f2, identity())
    )
    .get()


.thenCombine(location, (User us, GeoLocation loc) -> {
    List<CompletableFuture<Flight>> fs = agencies
        .stream()
        .map(agency -> agency.searchAsync(us, loc))
        .collect(toList());
    CompletableFuture[] fsArr = new CompletableFuture[fs.size()];
    fs.toArray(futuresArr);
    return CompletableFuture
        .anyOf(futuresArr)
        .thenApply(x -> ((Flight) x));
})


CompletableFuture<User> primaryFuture = //...
CompletableFuture<User> secondaryFuture = //...

CompletableFuture<LocalDate> ageFuture =
    primaryFuture
        .applyToEither(secondaryFuture,
	    user -> user.getBirth());


Observable<Observable<String> badStream = //...
Observable<String> goodStream = badStream.flatMap(x -> x);

CompletableFuture<CompletableFuture<String>> badFuture = //...
CompletableFuture<String> goodFuture = badFuture.thenCompose(x -> x);


CompletableFuture<Ticket> ticketFuture = flightFuture
        .thenCompose(flight -> bookAsync(flight));
