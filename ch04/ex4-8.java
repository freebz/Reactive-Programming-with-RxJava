// 스트림으로 콜백 대체하기

@Component
class JmsConsumer {

    @JmsListener(destination = "orders")
    public void newOrder(Message message) {
	//...
    }
}


private final PublishSubject<Message> subject = PublishSubject.create();

@JmsListener(destination = "orders", concurrency="1")
public void newOrder(Message msg) {
    subject.onNext(msg);
}

Observable<Message> observe() {
    return subject;
}


public Observable<Message> observe(
    ConnectionFactory connectionFactory,
    Topic topic) {
    return Observable.create(subscriber -> {
	try {
	    subscribeThrowing(subscriber, connectionFactory, topic);
	} catch (JMSException e) {
	    subscriber.onError(e);
	}
    });
}

private void subscribeThrowing(
            Subscriber<? super Message> subscriber,
            ConnectionFactory connectionFactory,
            Topic orders) throws JMSException {
    Connection connection = connectionFactory.createConnection();
    Session session = connection.createSession(true, AUTO_ACKNOWLEDGE);
    MessageConsumer consumer = session.createConsumer(orders);
    consumer.setMessageListener(subscriber::onNext);
    subscriber.add(onUnsubscribe(connection));
    connection.start();
}

private Subscription onUnsubscribe(Connection connection) {
    return Subscriptions.create(() -> {
	try {
	    connection.close();
	} catch (Exception e) {
	    log.error("Can't close", e);
	}
    });
}


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

ConnectionFactory connectionFactory =
    new ActiveMQConnectionFactory("tcp://localhost:61616");
Observable<String> txtMessages =
    observe(connectionFactory, new ActiveMQTopic("orders"))
    .cast(TextMessage.class)
    .flatMap(m -> {
	try {
	    return Observable.just(m.getText());
	} catch (JMSException e) {
	    return Observable.error(e);
	}
    });
