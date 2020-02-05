// 핵심 연산자: 매핑과 필터링

Observable<String> strings = //...
Observable<String> filtered = strings.filter(s -> s.startsWith("#"));


Observable<String> strings = someFileSource.lines();
Observable<String> comments = strings.filter(s -> s.startsWith("#"));
Observable<String> instructions = strings.filter(s -> s.startsWith(">"));
Observable<String> empty = strings.filter(String::isBlank);
