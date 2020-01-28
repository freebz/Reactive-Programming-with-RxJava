// 카디널리티(Cardinality)

// 이벤트 스트림

// 생산자
Observable<Event> mouseEvents = ...;

// 소비자
mouseEvents.subscribe(e -> doSomethingWithEvent(e));


// 생산자
Future<Event> mouseEvents = ...;

// 소비자
mouseEvents.onSuccess(e -> doSomethingWithEvent(e));
