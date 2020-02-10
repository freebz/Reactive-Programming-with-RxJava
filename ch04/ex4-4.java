// Observable 구성하기

List<Person> listPeople(int page) {
    return query(
  	    "SELECT * FROM PEOPLE ORDER BY id LIMIT ? OFFSET ?",
	    PAGE_SIZE,
	    page * PAGE_SIZE
    );
}


import static rx.Observable.defer;
import static rx.Observable.from;

Observable<Person> allPeople(int initialPage) {
    return defer(() -> from(listPeople(initialPage)))
	    .concatWith(defer(() ->
		    allPeople(initialPage + 1)));
}
