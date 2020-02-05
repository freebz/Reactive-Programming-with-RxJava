// 가변 누산기를 사용한 환산: collect()

Observable<List<Integer>> all = Observable
    .range(10, 20)
    .reduce(new ArrayList<>(), (list, item) -> {
        list.add(item);
        return list;
    });


Observable<List<Integer>> all = Observable
    .range(10, 20)
    .collect(ArrayList::new, List::add);


Observable<String> str = Observable
        .range(1, 10)
        .collect(
	        StringBuilder::new,
		(sb, x) -> sb.append(x).append(", "))
        .map(StringBuilder::toString);
