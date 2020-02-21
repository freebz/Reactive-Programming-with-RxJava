// 커맨드 일괄 처리와 응축

Observable<Book> allBooks() { /* ... */ }
Observable<Rating> fetchRating(Book book) { /* ... */ }


Observable<Rating> ratings = allBooks()
        .flatMap(this::fetchRating);


class FetchManyRatings extends HystrixObservableCommand<Rating> {

    private final Collection<Book> books;

    protected FetchManyRatings(Collection<Book> books) {
	super(HystrixCommandGroupKey.Factory.asKey("Books"));
	this.books = books;
    }

    @Override
    protected Observable<Rating> construct() {
	return fetchManyRatings(books);
    }
}


public class FetchRatingsCollapser
    extends HystrixObservableCollapser<Book, Rating, Rating, Book> {

    private final Book book;

    public FetchRatingsCollapser(Book book) {
	// 밑에서 설명하겠다
    }

    public Book getRequestArgument() {
	return book;
    }

    protected HystrixObservableCommand<Rating> createCommand(
	Collection<HystrixCollapser.CallapsedRequest<Rating, Book>> requests) {
	// 밑에서 설명하겠다
    }

    protected void onMissingResponse(
	    HystrixCollapser.CollapsedRequest<Rating, Book> r) {
	r.setException(new RuntimeException("Not found for: "
	+ r.getArgument()));
    }

    protected Func1<Book, Book> getRequestArgumentKeySelector() {
	return x -> x;
    }

    protected Func1<Rating, Rating> getBatchReturnTypeToResponseTypeMapper() {
	return x -> x;
    }

    protected Func1<Rating, Book> getBatchReturnTypeKeySelector() {
	return Rating::getBook;
    }
}


Observable<Rating> ratingObservable =
    new FetchRatingsCollapser(book).toObservable();


public FetchRatingsCollapser(Book book) {
    super(withCollapserKey(HystrixCollapserKey.Factory.asKey("Books"))
	    .andCollapserPropertiesDefaults(
		HystrixCollapserProperties
	            .Setter()
		    .withTimeDelayInMilliseconds(20)
	            .withMaxRequestsInBatch(50)
	    )
	    .andScope(Scope.GLOBAL));
    this.book = book;
}


protected HystrixObservableCommand<Rating> createCommand(
    Collection<HystrixCollapser.CollapsedRequest<Rating, Book>> requests) {
    List<Book> books = requests.stream()
	    .map(c -> c.getArgument())
	    .collect(toList());
    return new FetchManyRatings(books);
}
