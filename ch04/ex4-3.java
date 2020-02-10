// 느긋함 포용하기

public Observable<Person> listPeople() {
    return Observable.defer(() ->
        Observable.from(query("SELECT * FROM PEOPLE")));
}


void bestBookFor(Person person) {
    Book book;
    try {
	book = recommend(person);
    } catch (Exception e) {
	book = bestSeller();
    }
    display(book.getTitle());
}

void display(String title) {
    //...
}


void bestBookFor(Person person) {
    Observable<Book> recommended = recommend(person);
    Observable<Book> bestSeller = bestSeller();
    Observable<Book> book = recommended.onErrorResumeNext(bestSeller);
    Observable<String> title = book.map(Book::getTitle);
    title.subscribe(this::display);
}


void bestBookFor(Person person) {
    recommend(person)
	    .onErrorResumeNext(bestSeller())
    	    .map(Book::getTitle)
    	    .subscribe(this::display);
}
