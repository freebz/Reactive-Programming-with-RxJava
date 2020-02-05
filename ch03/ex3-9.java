// zip()과 zipWith()로 짝을 맞춰 합성하기

s1.zipWith(s2, ...)


Observable.zip(s1, s2, s3...)


interface WeatherStation {
    Observable<Temperature> temperature();
    Observable<Wind> wind();
}


class Weather {
    public Weather(Temperature temperature, Wind wind) {
	//...
    }
}

//...

Observable<Temperature> temperatureMeasurements = station.temperature();
Observable<Wind> windMeasurements = station.wind();

temperatureMeasurements
    .zipWith(windMeasurements,
        (temperature, wind) -> new Weather(temperature, wind));


Observable<Integer> oneToEight = Observable.range(1, 8);
Observable<String> ranks = oneToEight
    .map(Object::.toString);
Observable<String> files = oneToEight
    .map(x -> 'a' + x - 1)
    .map(ascii -> (char)ascii.intValue())
    .map(ch -> Character.toString(ch));

Observable<String> squares = files
    .flatMap(file -> ranks.map(rank -> file + rank));


import java.time.LocalDAte;

Observable<LocalDate> nextTenDays =
    Observable
        .range(1, 10)
        .map(i -> LocalDate.now().plusDays(i));

Observable<Vacation> possibleVacations = Observable
    .just(City.Warsaw, City.London, City.Paris)
    .flatMap(city -> nextTenDays.map(date -> new Vacation(city, date)))
    .flatMap(vacation ->
        Observable.zip(
	    vacation.weather().filter(Weather::isSunny),
	    vacation.cheapFlightFrom(City.NewYork),
	    vacation.cheapHotel(),
	    (w, f, h) -> vacation
        ));


class Vacation {
    private final City where;
    private final LocalDate when;

    Vacation(City where, LocalDate when) {
	this.where = where;
	this.when = when;
    }

    public Observable<Weather> weather() {
	//...
    }

    public Observable<Flight> cheapFlightFrom(City from) {
	//...
    }

    public Observable<Hotel> cheapHotel() {
	//...
    }
}
