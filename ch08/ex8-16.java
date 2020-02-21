// toList()와 buffer()로 이벤트 버퍼 처리

.window(100)
.flatMap(Observable:toList)


.buffer(100)


Observable<Incident> incidents = //...

Observable<Boolean> danger = incidents
        .buffer(1, TimeUnit.SECONDS)
        .map((List<Incidents> onSecond) -> oneSecond
	        .stream()
	        .filter(Incident::isHighPriority)
	        .count() > 5);


Observable<Boolean> danger = incidents
        .window(1, TimeUnit.SECONDS)
        .flatMap((Observable<Incident> oneSecond) ->
	        oneSecond
		        .filter(Incident::isHighPriority)
		        .count()
		        .map(c -> (c > 5))
	);
