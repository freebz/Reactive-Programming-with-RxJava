// UI 이벤트를 스트림처럼

compile 'com.jakewharton.rxbinding:rxbinding:0.4.0'


RxView
        .clicks(button)
        .flatMap(listCities(52.229841, 21.011736))
        .delay(2, TimeUnit.SECONDS)
        .concatMapIterable(extractCities())
        .map(toCityName())
        .toList()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
	        putOnListView(),
		displayError());

Func1<Void, Observable<Cities>> listCities(
        final double lat, final double lon) {
    return new Func1<Void, Observable<Cities>>() {
	@Override
	public Observable<Cities> call(Void aVoid) {
	    return meetup.listCities(lat, lon);
	}
    };
}


import android.widget.EditText;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;

EditText latText = //...
EditTExt lonText = //...

Observable<Double> latChanges = RxTextView
    .afterTextChangeEvents(latText)
    .flatMap(toDouble());
Observable<Double> lonChanges = RxTextView
    .afterTextChangeEvents(lonText)
    .flatMap(toDouble());

Observable<Cities> cities = Observable
    .combineLatest(latChanges, lonChanges, toPair())
    .debounce(1, TimeUnit.SECONDS)
    .flatMap(listCitiesNear());


Func1<TextViewAfterTextChangeEvent, Observable<Double>> toDouble() {
    return new Func1<TextViewAfterTextChangeEvent, Observable<Double>>() {
	@Override
	public Observable<Double> call(TextViewAfterTextChangeEvent e) {
	    String s = e.editable().toString();
	    try {
		return Observable.just(Double.parseDouble(s));
	    } catch (NumberFormatException e) {
		return Observable.empty();
	    }
	}
    };
}

//return Pair::new
Func2<Double, Double, Pair<Double, Double>> toPair() {
    return new Func2<Double, Double, Pair<Double, Double>>() {
	@Override
	public Pair<Double, Double> call(Double lat, Double lon) {
	    return new Pair<>(lat, lon);
	}
    };
}

//return latLon -> meetup.listCities(latLon.first, latLon.second)
Func1<Pair<Double, Double, Observable<Cities>> listCitiesNear() {
    return new Func1<Pair<Double, Double>, Observable<Cities>>() {
	@Override
	public Observable<Cities> call(Pair<Double, Double> latLon) {
	    return meetup.listCities(latLong.first, latLon.second);
	}
    };
}
