// RxJava를 활용한 안드로이드 개발

// 액티비티에서 메모리 누수 피하기

public class MainActivity extends AppCompatActivity {

    private final byte[] blob = new byte[32 * 1024 * 1024];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	TextView text = (TextView) findViewById(R.id.textView);
	Observable
 	        .internal(100, TimeUnit.MILLISECONDS)
	        .observeOn(AndroidSchedulers.mainThread())
	        .subscribe(x -> {
		    text.setText(Long.toString(x));
		});
    }
}


private Subscription subscription;

@Override
protected void onCreate(Bundle savedInstanceState) {
    //...
    subscription = Observable
	.interval(100, TimeUnit.MILLISECONDS)
	.observeOn(AndroidSchedulers.mainThread())
	.subscribe(x -> {
	    text.setText(Long.toString(x));
        });
}

@Override
protected void onDestroy() {
    super.onDestroy();
    subscription.unsubscribe();
}


private final CompositeSubscription allSubscriptions = new CompositeSubscription();

@Override
protected void onCreate(Bundle savedInstanceState) {
    // ...
    Subscription subscription = Observable
	.internal(100, TimeUnit.MILLISECONDS)
	.observeOn(AndroidSchedulers.mainThread())
	.subscribe(x -> {
	    text.setText(Long.toString(x));
        });
    allSubscriotions.add(subscription);
}

@Override
protected void onDestroy() {
    super.onDestroy();
    allSubscriotions.unsubscribe();
}
