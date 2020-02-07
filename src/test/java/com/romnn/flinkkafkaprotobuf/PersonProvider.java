package com.romnn.flinkkafkaprotobuf;

import java.util.ArrayList;
import java.util.Random;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import com.romnn.flinkkafkaprotobuf.protos.PersonProto.Person;

public class PersonProvider {

  private static Random gen = new Random();
  private static String[] names = new String[] {"Hans", "Peter", "Lisa"};

  public static Person getRandomPerson() {
    Person.Builder builder = Person.newBuilder();
    builder.setName(names[gen.nextInt(names.length)]);
    builder.setAge(gen.nextInt(100));
    return builder.build();
  }

  public static DataStream<Person> randomPersons() {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(4);
    
    ArrayList<Person> persons = new ArrayList<Person>();
    for (int i = 0; i < 200; i++) {
      persons.add(PersonProvider.getRandomPerson());
    };

    DataStream<Person> personsStream = env.fromCollection(persons);
    return personsStream;
  }

  public static DataStream<Person> deterministicPersons() {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(4);
    
    ArrayList<Person> persons = new ArrayList<Person>();
    for (int i = 0; i < 200; i++) {
      persons.add(Person.newBuilder().setName("Steve").setAge(25).build());
    };

    DataStream<Person> personsStream = env.fromCollection(persons);
    return personsStream;
  }

}
