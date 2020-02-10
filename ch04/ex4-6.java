// 명령형 방식의 동시성

Flight lookupFlight(String flightNo) {
    //...
}

Passenger findPassenger(long id) {
    //...
}

Ticket bookTicker(Flight flight, Passenger passenger) {
    //...
}


Flight flight = lookupFlight("LOT 783");
Passenger passenger = findPassenger(42);
Ticket ticket = bookTicker(flight, passenger);
sendEmail(ticket);


Observable<Flight> rxLookupFlight(String flightNo) {
    return Observable.defer(() ->
	    Observable.just(lookupFlight(flightNo)));
}

Observable<Passenger> rxFindPassenger(long id) {
    return Observable.defer(() ->
	    Observable.just(findPassenger(id)));
}


Observable<Flight> flight = rxLookupFlight("LOT 783");
Observable<Passenger> passenger = rxFindPassenger(42);
Observable<Ticket> ticket =
        flight.zipWith(passenger, (f, p) -> bookTicket(f, p));
ticket.subscribe(this::sendEmail);


Observable<Flight> flight =
    rxLookupFlight("LOT 783").subscribeOn(Schedulers.io());
Observable<Passenger> passenger =
    rxFindPassenger(42).subscribeOn(Schedulers.io());


rxLookupFlight("LOT 783")
    .subscribeOn(Schedulers.io())
    .timeout(100, TimeUnit.MILLISECONDS)


Observable<Ticket> rxBookTicket(Flight flight, Passenger passenger) {
    //...
}


import org.apache.commons.lang3.tuple.Pair;

Observable<Ticket> ticket = flight
        .zipWith(passenger, (Flight f, Passenger p) -> Pair.of(f, p))
        .flatMap(pair -> rxBookTicket(pair.getLeft(), pair.getRight()));


Observable<Ticket> ticket = flight
        .zipWith(passenger, this::rxBookTicket)
        .flatMap(obs -> obs);
