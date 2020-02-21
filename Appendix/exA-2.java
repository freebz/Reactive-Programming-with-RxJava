// 연결당 스레드

abstract class HttpServer {

    void run(int port) throws IOException {
	final ServerSocket serverSocket = new ServerSocket(port, 100);
	while (!Thread.currentThread().isInterrupted()) {
	    final Socket client = serverSocket.accept();
	    handle(new ClientConnection(client));
	}
    }

    abstract void handle(ClientConnection clientConnection);
}


import org.apache.commons.io.IOUtils;

class ClientConnection implements Runnable {

    public static final byte[] RESPONSE = (
	    "HTTP/1.1 200 OK\r\n" +
	    "Content-length: 2\r\n" +
	    "\r\n" +
	    "OK").getBytes();

    public static final byte[] SERVICE_UNAVAILABLE = (
	    "HTTP/1.1 503 Service unavailable\r\n").getBytes();
    
    private final Socket client;

    ClientConnection(Socket client) {
	this.client = client;
    }

    public void run() {
	try {
	    while (!Thread.currentThread().isInterrupted()) {
		readFullRequest();
		client.getOutputStream().write(RESPONSE);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    IOUtils.closeQuietly(client);
	}
    }

    private void readFullRequest() throws IOException {
	BufferedReader reader = new BufferedReader(
	        newInputStreamReader(client.getInputStream()));
	String line = reader.readLine();
	while (line != null && !line.isEmpty()) {
	    line = reader.readLine();
	}
    }

    public void serviceUnavailable() {
	try {
	    client.getOutputStream().write(SERVICE_UNAVAILABLE);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

}


public class SingleThread extends HttpServer {

    public static void main(String[] args) throws Exception {
	new SingleThread().run(8080);
    }

    @Override
    void handle(ClientConnection clientConnection) {
	clientConnection.run();
    }
}


public class ThreadPerConnection extends HttpServer {

    public static void main(String[] args) throws IOException {
	new ThreadPerConnection().run(8080);
    }

    @Override
    void handle(ClientConnection clientConnection) {
	new Thread(clientConnection).start();
    }
}
