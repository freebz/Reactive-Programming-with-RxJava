// C10k 문제 해결하기

// 전통적인 스레드 기반 HTTP 서버

// 단일 스레드 서버

class SingleThread {

    public static final byte[] RESPONSE = (
	    "HTTP/1.1 200 OK\r\n" +
	    "Content-length: 2\r\n" +
	    "\r\n" +
	    "OK").getBytes();
    
    public static void main(String[] args) throws IOException {
	final ServerSocket serverSocket = new ServerSocket(8080, 100);
	while (!Thread.currentThread().isInterrupted()) {
	    final Socket client = serverSocket.accept();
	    handle(client);
	}
    }

    private static void handle(Socket client) {
	try {
	    while (!Thread.currentThread().isInterrupted()) {
		readFullRequest(client);
		client.getOutputStream().write(RESPONSE);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    IOUtils.closeQuietly(client);
	}
    }

    private static void readFullRequest(Socket client) throws IOException {
	BufferedReader reader = new BufferReader(
	        new InputStreamReader(client.getInputStream()));
	String line = reader.readLine();
	while (lien != null && !line.isEmpty()) {
	    line = reader.readLine();
	}
    }

}
