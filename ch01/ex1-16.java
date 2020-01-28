// 다중 값

// 생산자
Observable<Friend> friends = ...

// 소비자
friends.subscribe(friend -> sayHello(friend));


// 생산자
Future<List<Friend>> friends = ...

// 소비자
friends.onSuccess(listOfFriends -> {
    listOfFriends.forEach(friend -> sayHello(friend));
}
