// 컬렉션에서 Observable로

class PersonDao {

    List<Person> listPeople() {
	return query("SELECT * FROM PEOPLE");
    }

    private List<Person> query(String sql) {
	//...
    }

}


Observable<Person> listPeople() {
    final List<Person> people = query("SELECT * FROM PEOPLE");
    return Observable.from(people);
}
