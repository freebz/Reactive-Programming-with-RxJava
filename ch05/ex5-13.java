// zip()을 사용하여 응답을 결합하기

import org.springframework.jdbc.core.JdbcTemplate;

//...

Single<String> content(int id) {
    return Single.fromCallable(() -> jdbcTemplate
        .queryForObject(
	    "SELECT content FROM articles WHERE id = ?",
	    String.class, id))
	.subscribeOn(Schedulers.io());
}

Single<Integer> likes(int id) {
    // 소셜 미디어 웹 사이트를 향한 비동기 HTTP 요청
}

Single<Void> updateReadCount() {
    // 그저 부수 효과일 뿐이며 Single의 반화 값은 없다
}


Single<Document> doc = Single.zip(
        content(123),
        likes(123),
        updateReadCount(),
        (con, lks, vod) -> buildHtml(con, lks)
);

//...

Document buildHtml(String content, int likes) {
    //...
}
