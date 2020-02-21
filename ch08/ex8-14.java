// 자바 8의 스트림과 CompletableFuture

// 병렬 스트림의 유용성

List<Person> people = //...

List<String> sorted = people
    .parallelStream()
    .filter(p -> p.getAge() >= 18)
    .map(Person::getFirstName)
    .sorted(Comparator.comparing(String::toLowerCase))
    .collect(toList());


// 이렇게 하면 안 된다
people
        .parallelStream()
        .forEach(this::publishOverJms);
