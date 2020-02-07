package com.romnn.flinkkafkaprotobuf;

import java.util.Properties;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import com.google.protobuf.Parser;
import org.apache.flink.dropwizard.metrics.DropwizardMeterWrapper;
import org.apache.flink.metrics.Meter;
import com.codahale.metrics.*;
import org.testcontainers.containers.KafkaContainer;
import com.romnn.flinkkafkaprotobuf.protos.PersonProto.Person;
import com.romnn.flinkkafkaprotobuf.PersonSerializer;
import com.romnn.flinkkafkaprotobuf.PersonDeserializer;
import com.romnn.flinkkafkaprotobuf.GenericBinaryProtoDeserializer;
import com.romnn.flinkkafkaprotobuf.GenericBinaryProtoSerializer;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;

import java.lang.Exception;
import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

  public static void main(String... args) throws Exception  {

    // Start a kafka container we can use for simulation
    KafkaContainer kafka = new KafkaContainer();
    kafka.start();
    System.out.println("Started kafka");
    
    // Setup the streaming execution environment
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);

    // Create a mock data stream of protobuf person instances
    Person.Builder testPerson = Person.newBuilder().setName("Roman");
    ArrayList<Person> persons = new ArrayList<Person>();
    Random gen = new Random();
    for (int i = 0; i < 200; i++) {
      persons.add(testPerson.setAge(gen.nextInt(100)).build());
    };
    DataStream<Person> personsStreamIn = env.fromCollection(persons);

    Properties producerProperties = new Properties();
    Properties consumerProperties = new Properties();
    producerProperties.setProperty("bootstrap.servers", kafka.getBootstrapServers());
    consumerProperties.setProperty("bootstrap.servers", kafka.getBootstrapServers());
    System.out.println(kafka.getBootstrapServers());
    producerProperties.setProperty("client.id", "PersonsProducerClient");
    consumerProperties.setProperty("client.id", "PersonsConsumerClient");

    // Create the topic first to make sure we can produce to it
    AdminClient adminClient = AdminClient.create(consumerProperties);
    adminClient.createTopics(Arrays.asList(new NewTopic("PERSONS_TOPIC", 1, (short)1)));
    adminClient.close();
    
    // Serializers
    GenericBinaryProtoSerializer<Person> genericSerializer = new GenericBinaryProtoSerializer<>();
    PersonSerializer specificSerializer = new PersonSerializer();
    
    // Producer
    FlinkKafkaProducer011<Person> personsKafkaProducer =
      new FlinkKafkaProducer011<Person>("PERSONS_TOPIC", genericSerializer, producerProperties);
    personsStreamIn.addSink(personsKafkaProducer);

    // Deserializers
    GenericBinaryProtoDeserializer<Person> genericDeserializer = new GenericBinaryProtoDeserializer<>(Person.class);
    PersonDeserializer specificDeserializer = new PersonDeserializer();
    
    // Consumer
    FlinkKafkaConsumer011<Person> personsKafkaConsumer =
        new FlinkKafkaConsumer011<Person>("PERSONS_TOPIC", genericDeserializer, consumerProperties);

    // Transformation and processing of consumed events
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
