// RxJava 지원을 포함하는 레트로핏

compile 'com.squareup.retrofit2:retrofit:2.0.1'
compile 'com.squareup.retrofit2:adapter-rxjava:2.0.1'
compile 'com.squareup.retrofit2:converter-jackson:2.0.1'


import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MeetupApi {

    @GET("/2/cities")
    Observable<Cities> listCities(
	@Query("lat") double lat,
	@Query("lon") double lon
    );

}


public class Cities {
    private List<City> results;
}

public class City {
    private String city;
    private String country;
    private Double distance;
    private Integer id;
    private Double lat;
    private String localizedCountryName;
    private Double lon;
    private Integer memberCount;
    private Integer ranking;
    private String zip;
}


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

ObjectMapper objectMapper = new ObjectMapper()
objectMapper.setPropertyNamingStrategy(
    PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
objectMapper.configure(
    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

Retrofix retrofit = new Retrofit.Builder()
    .baseUrl("https://api.meetup.com/")
    .addCallAdapterFactory(
	RxJavaCallAdapterFactory.create())
    .addConverterFactory(
	JacksonConverterFactory.create(objectMapper))
    .build();


MeetupApi meetup = retrofit.create(MeetupApi.class);


double warsawLat = 52.229841;
double warsawLon = 21.011736;
Observable<Cities> cities = meetup.listCities(warsawLat, warsawLon);
Observable<City> citiObs = cities
        .concatMapIterable(Cities::getResults);
Observable<String> map = cityObs
        .filter(city -> city.distanceTo(warsawLat, warsawLon) < 50)
        .map(City::getCity);


public interface GeoNames {

    @GET("/searchJSON")
    Observable<SearchResult> search(
	@Query("q") String query,
	@Query("maxRows") int maxRows,
	@Query("style") String style,
	@Query("username") String username);
}


class SearchResult {
    private List<Geoname> geonames = new ArrayList<>();
}

public class Geoname {
    private String lat;
    private String lng;
    private Integer geonameId;
    private Integer population;
    private String countryCode;
    private String name;
}


GeoNames geoNames = new Retrofit.Builder()
    .baseUrl("http://api.geonames.org")
    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    .addConverterFactory(JacksonConverterFactory.create(objectMapper))
    .build()
    .create(GeoNames.class);


Observable<Long> totalPopulation = meetup
        .listCities(warsawLat, warsawLon)
        .concatMapIterable(Cities::getResults)
        .filter(city -> city.distanceTo(warsawLat, warsawLon) < 50)
        .map(City::getCity)
        .flatMap(getNames::populationOf)
        .reduce(0L, (x, y) -> x + y);


public interface GeoNames {
    
    default Observable<Integer> populationOf(String query) {
	return search(query)
	    .concatMapIterable(SearchResult::getGeonames)
	    .map(Geoname::getPopulation)
	    .filter(p -> p != null)
	    .singleOrDefault(0)
	    .doOnError(th ->
	    log.warn("Falling back to 0 for {}", query, th))
	    .onErrorReturn(th -> 0)
	    .subscribeOn(Schedulers.io());
    }

    default Observable<SearchResult> search(String query) {
	return search(query, 1, "LONG", "some_user");
    }

    @GET("/searchJSON")
    Observable<SearchResult> search(
	@Query("q") String query,
	@Query("maxRows") int maxRows,
	@Query("style") String style,
	@Query("username") String username
    );

}
