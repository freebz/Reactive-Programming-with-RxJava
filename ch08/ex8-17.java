// cache()와 ReplaySubject로 캐시 처리

// 메모리 점유율이 낮은 배압

Observable<Picture> fast = Observable
        .interval(10, MICROSECONDS)
        .map(Picture::new);
Observable<Picture> slow = Observable
        .interval(11, MICROSECONDS)
        .map(Picture::new);

Observable
        .zip(fast, slow, (f, s) -> f + " : " + s)


Observable
        .zip(
	        fast.onBackpressureDrop(),
		slow.onBackpressureDrop(),
		(f, s) -> f + " : " + s)
