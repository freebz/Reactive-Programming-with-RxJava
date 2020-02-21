// 커넥션 스레드 풀

class ThreadPool extends HttpServer {

    private static void main(String[] args) throws IOException {
	new ThreadPool().run(8080);
    }

    public ThreadPool() {
	BlockingQueue<Runaable> workQueue = new ArrayBlockingQueue<>(1000);
	executor = new ThreadPoolExecutor(100, 100, 0L,
				MILLISECONDS, workQueue,
	        (r, ex) -> {
		    ((ClientConnection) r).serviceUnavailable();
		});
    }

    @Override
    void handle(ClientConnection clientConnection) {
	executor.execute(clientConnection);
    }

}
