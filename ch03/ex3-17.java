// 스트림을 결합하는 방법: concat()과 merge(), switchOnNext()

Observable<Data> veryLong = //...
final Observable<Data> ends = Observable.concat(
	veryLong.take(5),
	veryLong.takeLast(5)
);


Observable<Car> fromCache = loadFromCache();
Observable<Car> fromDb = loadFromDb();

Observable<Car> found = Observable
        .concat(fromCache, fromDb)
        .first();


import org.apache.commons.lang3.tuple.Pair;

Observable<String> speak(String quote, long millisPerChar) {
    String[] tokens = quote.replaceAll("[:,]", "").split(" ");
    Observable<String> words = Observable.from(tokens);
    Observable<Long> absoluteDelay = words
	.map(String::length)
	.map(len -> len * millisPerChar)
	.scan((total, current) -> total + current);
    return words
	.zipWith(absoluteDelay.startWith(0L), Pair::of)
	.flatMap(pair -> just(pair.getLeft())
	    .delay(pair.getRight(), MILLISECONDS));
}


Observable<String> alice = speak(
        "To be, or not to be: that is the question", 110);
Observable<String> bob = speak(
        "Though this be madness, yet there is method in't", 90);
Observable<String> jane = speak(
        "There are more things in Heaven and Earth, " +
        "Horatio, than are dreamt of in your philosophy", 100);


Observable
    .merge(
	alice.map(w -> "Alice: " + w),
	bob.map(w   -> "Bob:   " + w),
	jane.map(w  -> "Jane:  " + w)
    )
.subscribe(System.out::println);


/*
Alice: To
Bob:   Though
Jane:  There
Alice: be
Alice: or
Jane:  are
Alice: not
Bob:   this
Jane:  more
Alice: to
Jane:  things
Alice: be
Bob:   be
Alice: that
Bob:   madness
Jane:  in
Alice: is
Jane:  Heaven
Alice: the
Bob:   yet
Alice: question
Jane:  and
Bob:   there
Jane:  Earth
Bob:   is
Jane:  Horatio
Bob:   method
Jane:  than
Bob:   in't
Jane:  are
Jane:  dreamt
Jane:  of
Jane:  in
Jane:  your
Jane:  philosophy
*/


Observable
    .concat(
	alice.map(w -> "Alice: " + w),
	bob.map(w   -> "Bob:   " + w),
	jane.map(w  -> "Jane:  " + w)
    )
.subscribe(System.out::println);


/*
Alice: To
Alice: be
Alice: or
Alice: not
Alice: to
Alice: be
Alice: that
Alice: is
Alice: the
Alice: question
Bob:   Though
Bob:   this
Bob:   be
Bob:   madness
Bob:   yet
Bob:   there
Bob:   is
Bob:   method
Bob:   in't
Jane:  There
Jane:  are
Jane:  more
Jane:  things
Jane:  in
Jane:  Heaven
Jane:  and
Jane:  Earth
Jane:  Horatio
Jane:  than
Jane:  are
Jane:  dreamt
Jane:  of
Jane:  in
Jane:  your
Jane:  philosophy
*/


import java.util.Random;

Random rnd = new Random();
Observable<Observable<String>> quotes = just(
                alice.map(w -> "Alice: " + w),
                bob.map(w   -> "Bob:   " + w),
                jane.map(w  -> "Jane:  " + w));


// A
map(innerObs ->
        innerObs.dealy(rnd.nextInt(5), SECONDS))

// B
flatMap(innerObs -> just(innerObs)
        .delay(rnd.nextInt(5), SECONDS))


Random rnd = new Random();
Observable<Observable<String>> quotes = just(
                alice.map(w -> "Alice: " + w),
                bob.map(w   -> "Bob:   " + w),
                jane.map(w  -> "Jane:  " + w))
        .flatMap(innerObs -> just(innerObs)
		.delay(rnd.nextInt(5), SECONDS));

Observable
        .switchOnNext(quotes)
        .subscribe(System.out::println);


/*
Jane:  Threr
Jane:  are
Jane:  more
Alice: To
Alice: be
Alice: or
Alice: not
Alice: to
Bob:   Though
Bob:   this
Bob:   be
Bob:   madness
Bob:   yet
Bob:   there
Bob:   is
Bob:   method
Bob:   it't
*/
