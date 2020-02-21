// NoSQL 데이터베이스 질의

// 카우치베이스 클라이언트 API

{
  "id": 14197,
  "type": "route",
  "airline": "B6",
  "sourceairport": "PHX",
  "destinationairport": "BOS",
  "stops": 0,
  "schedule": [
    {
      "day": 0,
      "utc": "22:12:00",
      "flight": "B6928"
    },
    {
      "day": 0,
      "utc": "06:40:00",
      "flight": "B6387"
    },
    ...
    {
      "day": 1,
      "utc": "08:16:00",
      "flight": "B6922"
    }
    ...


CouchbaseCluster cluster = CouchbaseCluster.create();
cluster
    .openBucket("travel-sample")
    .get("route_14197")
    .map(AbstractDobument::content)
    .map(json -> json.getArray("schedule"))
    .concatMapIterable(JsonArray::toList)
    .cast(Map.class)
    .filter(m -> ((Number)m.get("day")).intValue() == 0)
    .map(m -> m.get("flight").toString())
    .subscribe(flight -> System.out.println(flight));
