# NoSQL

* MongoDB
  ```
  # application.properties
  spring.data.mongodb.uri = mongodb://localhost:27017/ecommerce
  spring.data.mongodb.database = ecommerce
  ```
  ```java
  import org.springframework.data.mongodb.core.mapping.Document;

  @Document(collection = "products")
  public class Product {
    @Id private String id;
 
  }
  // // //
  import org.springframework.data.mongodb.repository.MongoRepository;

  public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByCategory(String category);  

  }
  ```

   * **mongosh**
   ```
   db.myCollection.insertOne({
     name: "Ada Lovelace",
     contribution: "First computer programmer" });
   ```
   `updateOne(filter, update, options)`
   Let's say you have a document in a users collection like this:
   ```json
   {
    "_id": "someId",
    "username": "jane",
    "email": "jane.doe@example.com",
    "status": "active",
    "loginCount": 5
   }
   ```
   Now, you want to update `jane`'s status to "inactive" and increment her loginCount
   without changing her username or email.
   ```
   db.users.updateOne(
    { "username": "jane_doe" },  // Filter: find the user with this username
    {
      $set: {
        "status": "inactive",    // Set the 'status' field
        "loginCount": 6          // Set the 'loginCount' field
      }
    }
   )
   ```
  * Pure Java
  ```java
  import com.mongodb.client.MongoClients;
  import com.mongodb.client.MongoClient;
  import com.mongodb.client.MongoDatabase;
  import com.mongodb.client.MongoCollection;
  import org.bson.Document;
  import com.mongodb.client.model.Updates;
  import org.bson.conversions.Bson;

  import static com.mongodb.client.model.Filters.eq;

  void insertDocument() {
   try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
     MongoDatabase database = mongoClient.getDatabase("myDatabase");
     MongoCollection<Document> collection = database.getCollection("myCollection");

     Document newDoc = new Document("name", "Charles Babbage")
                             .append("contribution", "Difference Engine");
     collection.insertOne(newDoc);
     System.out.println("Document inserted successfully!");
   }
  }

  void updateDocument() {
    try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
      MongoDatabase database = mongoClient.getDatabase("myDatabase");
      MongoCollection<Document> collection = database.getCollection("users");

      Bson filter = eq("username", "jane_doe");
      Bson updateOperation = Updates.combine(
        Updates.set("status", "inactive"),
        Updates.set("loginCount", 6)
      );
   // Or more concisely for just $set:
      Bson updateOperation = Updates.set("status", "inactive");
      updateOperation = Updates.combine(updateOperation, Updates.set("loginCount", 6));
   // Or even more directly if you construct the Document yourself:
      Bson updateOperation = new Document("$set",
         new Document("status", "inactive").append("loginCount", 6));

   // Finally
    collection.updateOne(filter, updateOperation);
    System.out.println("User status updated!");
   }
  }
  ```


* InfluxDB
  ```
  # application.properties
  spring.influx.url = http://localhost:8086
  spring.influx.org = mt-org
  spring.influx.token = my-token
  spring.influx.bucket = sensor_data 
  ```
  ```java
  import org.springframework.data.influxdb.mapping.InfluxMeasurement;

  @InfluxMeasurement(name = "sensor_data")
  public class SensorData {
    @Id private String id;
    private String sensorId;
    private double temperature;
    private long timestamp;

  }
  // // //
  import com.influxdb.client.InfluxDBClient;
  import com.influxdb.client.InfluxDBClientFactory;
  import com.influxdb.client.WriteApi;

  public class InfluxDBService {

    private InfluxDBClient client = InfluxDBClient.create(url, token.toCharArray());
   
    // "SELECT * FROM sensor_data WHERE time > now() - 1h"
    // "DELETE FROM sensor_data WHERE time < now() - 14d"

  }
  ```

* Neo4J
  ```
  # application.properties
  spring.neo4j.uri = bolt://localhost:7687
  spring.neo4j.authentication.username = neo4j
  spring.neo4j.authentication.password = 124816
  ```
  ```java
  import org.springframework.data.neo4j.core.schema.*;

  @Node public class User {
    @Id @GeneratedValue private Long id;

    @Relationship(type = "FRIENDS_WITH", direction = Relationship.Direction.OUTGOING)
    private List<User> friends;

  }
  // // //
  import org.springframework.data.neo4j.repository.Neo4jRepository;

  public interface UserRepository extends Neo4jRepository<User, Long> {

    User findByName(String name);

  }
  ``` 
