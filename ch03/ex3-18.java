// 주어진 기준으로 스트림을 나누는 groupBy()

FactStore factStore = new CassandraFactStore();
Observable<ReservationEvent> facts = factStore.observe();
facts.subscribe(this::updateProjection);

//...

void updateProjection(ReservationEvent event) {
    UUID uuid = event.getReservationUuid();
    Reservation res = loadBy(uuid)
	.orElseGet(() -> new Reservation(uuid));
    res.consume(event);
    store(event.getUuid(), res);
}

private void store(UUID id, Reservation modified) {
    //...
}

Optional<Reservation> loadBy(UUID uuid) {
    //...
}

class Reservation {

    Reservation consume(ReservationEvent event) {
	// 자신의 상태를 바꿈
	return this;
    }
}


Observable<ReservationEvent> racts = factStore.observe();

facts
        .flatMap(this::updateProjectionAsync)
        .subscribe();

//...

Observable<ReservationEvent> updateProjectionAsync(ReservationEvent event) {
    // 아마도 비동기
}


Observable<ReservationEvent> facts = factStore.observe();

Observable<GroupedObservable<UUID, ReservationEvent>> grouped =
        facts.groupBy(ReservationEvent::getReservationUuid);

grouped.subscribe(byUuid -> {
    byUuid.subscribe(this::updateProjection);
});
