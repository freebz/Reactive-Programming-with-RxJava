// 안드로이드에서 Scheduler 사용하기

compile 'io.reactivex:rxandroid:1.1.0'


button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
	meetup
	    .listCities(52.229841, 21.011736)
	    .concatMapIterable(extractCities())
	    .map(toCityName())
	    .toList()
	    .subscribeOn(Schedulers.io())
	    .observeOn(AndroidSchedulers.mainThread())
	    .subscribe(
		    putOnListView(),
		    displayError());
    }

    //...

});


//Cities::getResults
Func1<Cities, Iterable<City>> extractCities() {
    return new Func1<Cities, Iterable<City>>() {
	@Override
	public Iterable<City> call(Cities cities) {
	    return cities.getResults();
	}
    };
}

//City:getCity
Func1<City, String> toCityName() {
    return new Func1<City, String>() {
	@Override
	public String call(City city) {
	    return city.getCity();
	}
    };
}

//cities -> listView.setAdapter(...)
Action1<List<String>> putOnListView() {
    return new Action1<List<String>>() {
	@Override
	public void call(List<String> citeis) {
	    listView.setAdapter(new ArrayAdapter(
		    MainActivity.this, R.layout.list, cities));
	}
    };
}
//throwable -> {...}
Action1<Throwable> displayError() {
    return new Action1<Throwable>() {
	@Override
	public void call(Throwable throwable) {
	    Log.e(TAG, "Error", throwable);
	    Toast.makeText(MainActivity.this,
			   "Unable to load cities",
			   Toast.LENGTH_SHORT)
		.show();
	}
    };
}
