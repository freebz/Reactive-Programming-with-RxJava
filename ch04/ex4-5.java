// 느긋한 페이지 분할과 이어 붙이기

Observable<List<Person>> allPages = Observable
            .range(0, Integer.MAX_VALUE)
            .map(this::listPeople)
            .takeWhile(list -> list.isEmpty());


Observable<Person> people = allPages.concatMap(Observable::from);


Observable<Person> people = allPages.concatMapIterable(page -> page);
