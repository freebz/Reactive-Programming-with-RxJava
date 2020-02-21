// 메모리 소비와 누수

// 메모리 소모량을 통제할 수 없는 연산자

// 모든 이벤트를 캐시에 저장하는 distinct()

class Picture {
    private final byte[] blob = new byte[128 * 1024];
    private final long tag;

    Picture(long tag) { this.tag = tag; }

    @Override
    public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Picture)) return false;
	Picture picture = (Picture) o;
	return tag == picture.tag;
    }

    @Override
    public int hashCode() {
	return (int) (tag ^ (tag >>> 32));
    }

    @Override
    public String toString() {
	return Long.toString(tag);
    }
}


Observable
    .range(0, Integer.MAX_VALUE)
    .map(Picture::new)
    .distinct()
    .sample(1, TimeUnit.SECONDS)
    .subscribe(System.out::println);


distinct(Picture::getTag)


Observable
        .range(0, Integer.MAX_VALUE)
        .map(Picture::new)
        .window(1, TimeUnit.SECONDS)
        .flatMap(Observable::count)
        .subscribe(System.out.println);


Observable
        .range(0, Integer.MAX_VALUE)
        .map(Picture::new)
        .window(10, TimeUnit.SECONDS)
        .flatMap(Observable::distinct)
