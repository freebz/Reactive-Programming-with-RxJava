// BlockingObservable: 리액티브 세상에서 벗어나기

List<Person> people = personDao.listPeople();
String json = marshal(people);


Observable<Person> peopleStream = personDao.listPeople();
Observable<List<Person>> peopleList = peopleStream.toList();
BlockingObservable<List<Person>> peopleBlocking = peopleList.toBlocking();
List<Person> people = peopleBlocking.single();


List<Person> people = personDao
    .listPeople()
    .toList()
    .toBlocking()
    .single();
