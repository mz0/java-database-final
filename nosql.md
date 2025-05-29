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
