// 밀어내기와 끌어오기

interface Observable<T> {
    Subscription subscribe(Observer s)
}
