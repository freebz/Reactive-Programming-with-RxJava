// PostgreSQL에서 NOTIFY와 LISTEN 사례 연구

NOTIFY my_channel;
NOTIFY my_channel, '{"answer": 42}';


try (Connection connection =
    DriverManager.getConnection("jdbc:postgresql:db")) {
        try (Statement statement = connection.createStatement()) {
	    statement.execute("LISTEN my_channel");
	}
	Jdbc4Connection pgConn = (Jdbc4Connection) connection;
	pollForNotifications(pgConn);
    }
}

//...

void pollForNotifications(Jdbc4Connection pgConn) throws Exception {
    while (!Thread.currentThread().isInterrupted()) {
	final PGNotification[] notifications = pgConn.getNotifications();
	if (notifications != null) {
	    for (final PGNotification notification : notifications) {
		System.out.println(
		        notification.getName() + ": " +
			notification.getParameter());
	    }
	}
	TimeUnit.MILLISECONDS.sleep(100);
    }
}


Observable<PGNotification> observe(String channel, long pollingPeriod) {
    return Observable.<PGNotification>create(subscriber -> {
	try {
	    Connection connection = DriverManager
	  	    .getConnection("jdbc:postgresql:db");
	    subscriber.add(Subscriptions.create(() ->
		    closeQuietly(connection)));
	    listenOn(connection, channel);
	    Jdbc4Connection pgConn = (Jdbc4Connection) connection;
	    pollForNotifications(pollingPeriod, pgConn)
	 	    .subscribe(Subscribers.wrap(subscriber));
	} catch (Exception e) {
	    subscriber.onError(e);
	}
    }).share();
}

void listenOn(Connection connection, String channel) throws SQLException {
    try (Statement statement = connection.createStatement()) {
	statement.execute("LISTEN " + channel);
    }
}

void closeQuietly(Connection connection) {
    try {
	connection.close();
    } catch (SQLException e) {
	e.printStackTrace();
    }
}


Observable<PGNotification> pollForNotifications(
            long pollingPeriod,
            AbstractJdbc2Connection pgConn) {
    return Observable
	    .interval(0, pollingPeriod, TimeUnit.MILLISECONDS)
	    .flatMap(x -> tryGetNotification(pgConn))
	    .filter(arr -> arr != null)
    	    .flatMapIterable(Arrays::asList);
}

Observable<PGNotification[]> tryGetNotification(
            AbstractJdbc2Connection pgConn) {
    try {
	return Observable.just(pgConn.getNotifications());
    } catch (SQLException e) {
	return Observable.error(e);
    }
}
