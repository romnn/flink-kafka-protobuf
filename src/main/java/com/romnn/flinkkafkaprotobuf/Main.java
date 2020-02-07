package com.romnn.flinkkafkaprotobuf;

import java.util.Properties;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import com.google.protobuf.Parser;
// import com.twitter.chill.protobuf.ProtobufSerializer;
import org.testcontainers.containers.KafkaContainer;
import com.romnn.flinkkafkaprotobuf.protos.PersonProto.Person;
import com.romnn.flinkkafkaprotobuf.PersonSerializer;
import com.romnn.flinkkafkaprotobuf.PersonDeserializer;
import java.lang.Exception;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

  public static void main(String... args) throws Exception  {

    // Start a kafka container we can use for simulation
    KafkaContainer kafka = new KafkaContainer();
    kafka.start();
    
    // Setup the streaming execution environment
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(5);

    // env.getConfig().registerTypeWithKryoSerializer(LiveTrainData.class, ProtobufSerializer.class);
    Person.Builder testPerson = Person.newBuilder().setName("Roman");
    ArrayList<Person> persons = new ArrayList<Person>();
    Random gen = new Random();
    for (int i = 0; i < 200; i++) {
      persons.add(testPerson.setAge(gen.nextInt(100)).build());
    };
    DataStream<Person> personsStreamIn = env.fromCollection(persons);

    Properties properties = new Properties();
    properties.setProperty("bootstrap.servers", "localhost:29092");
    properties.setProperty("group.id", "PersonsConsumer");
    properties.setProperty("client.id", "PersonsConsumer");
    
    FlinkKafkaProducer011<Person> personsKafkaProducer =
      new FlinkKafkaProducer011<Person>("PERSONS_TOPIC", new PersonSerializer(), properties);

    personsStreamIn.addSink(personsKafkaProducer);

    FlinkKafkaConsumer011<Person> personsKafkaConsumer =
        new FlinkKafkaConsumer011<Person>("PERSONS_TOPIC", new PersonDeserializer(), properties);

    // Add consumer as source for data stream
    DataStream<Person> personStreamOut = env.addSource(personsKafkaConsumer);
    DataStream<Person> adultPersonStream = personStreamOut.filter(person -> person.getAge() >= 18);
    DataStream<String> result = adultPersonStream.map(new MapFunction<Person, String>() {
      @Override
      public String map(Person person) {
          return String.format("The Person %s is adult (age %d)", person.getName(), person.getAge());
      }
    });
    result.print();
    env.execute("Flink Streaming Java API Skeleton");
    kafka.stop();
  }
}
