// HystricObservableCommand를 사용한 논블로킹 커맨드

public class CitiesCmd extends HystrixObservableCommand<Cities> {

    private final MeetupApi api;
    private final double lat;
    private final double lon;

    protected CitiesCmd(MeetupApi api, double lat, double lon) {
	super(HystrixCommandGroupKey.Factory.asKey("Meetup"));
	this.api = api;
	this.lat = lat;
	this.lon = lon;
    }

    @Override
    protected Observable<Cities> construct() {
	return api.listCities(lat, lon);
    }
}
