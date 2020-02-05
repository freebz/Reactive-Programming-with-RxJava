// flatMap()의 동시성 제어

class User {
    Observable<Profile> loadProfile() {
	//HTTP 요청 수행
    }
}

class Profile {/* ... */}

//...

List<User> veriLargeList = //...
Observable<Profile> profiles = Observable
        .from(veriLargeList)
        .flatMap(User::loadProfile);


flatMap(User::loadProfile, 10);
