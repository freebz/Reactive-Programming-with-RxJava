// map() 연산자 내부 구현 들여다보기

public final class OperatorMap<T, R> implements Operator<R, T> {

    private final Func<T, R> transformer;

    public OperatorMap(Func1<T, R> transformer) {
	this.transformer = transformer;
    }

    @Override
    public Subscriber<T> call(final Subscriber<R> child) {
	return new Subscriber<T>(child) {

	    @Override
	    public void onCompleted() {
		child.onCompleted();
	    }

	    @Override
	    public void onError(Throwable e) {
		child.onError(e);
	    }

	    @Override
	    public void onNext(T t) {
		try {
		    child.onNext(transformer.call(t));
		} catch (Exception e) {
		    onError(e);
		}
	    }
	};
    }
}
