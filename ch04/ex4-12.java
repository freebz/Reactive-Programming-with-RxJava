// Scheduler 세부 구현 내용 살펴보기

abstract class Scheduler {
    abstract Worker createWorker();

    long now();

    abstract static class Worker implements Subscription {

	abstract Subscription schedule(Action0 action);

	abstract Subscription schedule(Action0 action,
			    long delayTime, TimeUnit unit);

	long now();
    }
}


package rx.android.schedulers;

import android.os.Handler;
import android.os.Lopper;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.internal.schedulers.ScheduledAction;
import rx.subscriptions.Subscriptions;

import java.util.concurrent.TimeUnit;

public final class SimplifiedHandlerScheduler extends Scheduler {

    @Override
    public Worker createWorker() {
	return new HandlerWorker();
    }
    
    static class SimplifiedHandlerWorker extends Worker {

	private final Handler handler = new Handler(Looper.getMainLopper());

	@Override
	public void unsubscribe() {
	    // 조만간 구현할 예정
	}
	
	@Override
	public boolean isUnsubscribed() {
	    // 조만간 구현할 예정
	    return false;
	}

	@Override
	public Subscription schedule(final Action0 action) {
	    return schedule(action, 0, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public Subscription schedule(
	Action0 action, long delayTime, TimeUnit unit) {
	    ScheduledAction scheduledAction = new ScheduledAction(action);
	    handler.postDelayed(scheduledAction, unit.toMillis(delayTime));
	    
	    scheduledAction.add(Subscriptions.create(() ->
	            handler.removeCallbacks(scheduledAction)));

	    return scheduledAction;
	}
    }
}


private final CompositeSubscription compositeSubscription =
    new CompositeSubscription();

@Override
public void unsubscribe() {
    compositeSubscription.unsubscribe();
}

@Override
public boolean isUnsubscribed() {
    return compositeSubscription.isUnsubscribed();
}

@Override
public Subscription schedule(Action0 action, long delayTime, TimeUnit unit)
{
    if (compositeSubscription.isUnsubscribed()) {
	return Subscriptions.unsubscribed();
    }

    final ScheduledAction scheduledAction = new ScheduledAction(action);
    scheduledAction.addParent(compositeSubscription);
    compositeSubscription.add(scheduledAction);

    handler.postDelayed(scheduledAction, unit.toMillis(delayTime));

    scheduledAction.add(Subscriptions.create(() ->
            handler.removeCallbacks(scheduledAction)));

    return scheduledAction;
}
