// 카프카에서 메시지 받기

reactiveCamel
        .toObservable("kafka:localhost:9092?topic=demo&groupId=rx")
        .map(Message::getBody)
        .subscribe(e ->
	        log.info("Message: {}", e));
