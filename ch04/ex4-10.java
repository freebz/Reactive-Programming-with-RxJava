// RxJava의 멀티 스레딩

void log(Object label) {
    System.out.println(
	System.currentTimeMillis() - start + "\t| " +
	Thread.currentThread().getName()   + "\t| " +
	label);
}
