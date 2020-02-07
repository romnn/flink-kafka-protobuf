package com.romnn.flinkkafkaprotobuf;

import org.testcontainers.containers.KafkaContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamUtils;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011.Semantic;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import java.lang.Long;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Arrays;
import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Properties;
import com.romnn.flinkkafkaprotobuf.protos.PersonProto.Person;
import com.romnn.flinkkafkaprotobuf.PersonSerializer;
import com.romnn.flinkkafkaprotobuf.PersonDeserializer;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import java.util.Optional;

/**
 * Integration test for Protobuf serializing and deserializing to kafka topic
 * for the flink kafka connector.
 */
@RunWith(Parameterized.class)
public class PersonIntegrationTest {

    // private static KafkaContainer kafka;
    private Properties kafkaProperties;

    private SerializationSchema<Person> serializer;
    private DeserializationSchema<Person> deserializer;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {     
                 { new PersonSerializer(), new PersonDeserializer() },
                 { new PersonSerializer(), new PersonDeserializer() },
           });
    }

    public PersonIntegrationTest(SerializationSchema<Person> ser, DeserializationSchema<Person> deser) {
        try {
            // kafka.stop();
            // System.out.println("Stopped kafka");
            // kafka.start();
            // System.out.println("Started kafka");
        } catch (NullPointerException ex) {
            // Ignore
            // kafka = new KafkaContainer();
            // kafka.start();
        }
        // kafka = new KafkaContainer();
        // kafka.start();
        // System.out.println("Started kafka");
        this.serializer = ser;
        this.deserializer = deser;
    }

    /*
    @BeforeClass
    public static void startContainer() {
        System.out.println("Started kafka");
        kafka = new KafkaContainer();
        kafka.start();
    }*/

    
    @AfterClass
    public static void stopContainer() {
        // kafka.stop();
        // System.out.println("Stopped kafka");
    }
    
    
    /*
    @Before
    public void startContainer() {
        System.out.println("Started kafka");
        kafka.start();
    }*/

    @After
    public void stopContainer2() {
        // System.out.println("Stopped kafka");
        // kafka.stop();
    }

    @Before
    public void setUp() {
        kafkaProperties = new Properties();
        // kafkaProperties.setProperty("bootstrap.servers", kafka.getBootstrapServers());
        kafkaProperties.setProperty("group.id", "PersonsConsumer");
        kafkaProperties.setProperty("client.id", "PersonsConsumer");
        kafkaProperties.setProperty("transactional.id", "my-transaction");
        kafkaProperties.setProperty("isolation.level", "read_committed");
    }

    public static Properties getProps(KafkaContainer kafka) {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", kafka.getBootstrapServers());
        props.setProperty("group.id", "PersonsConsumer");
        props.setProperty("client.id", "PersonsConsumer");
        props.setProperty("transactional.id", "my-transaction");
        props.setProperty("isolation.level", "read_committed");
        return props;
    }

    @Test
    public void testSerialization() throws Exception {
        KafkaContainer kafka = new KafkaContainer();
        kafka.start();
        DataStream<Person> inputStream = PersonProvider.randomPersons();
        FlinkKafkaProducer011<Person> personsKafkaProducer = new FlinkKafkaProducer011<Person>("PERSONS_TOPIC",
                serializer, getProps(kafka));

        inputStream.addSink(personsKafkaProducer);
        expectedStream.getExecutionEnvironment().execute("Test");
        kafka.stop();
    }

    @Test
    public void testDeserialization() throws Exception {
        KafkaContainer kafka = new KafkaContainer();
        kafka.start();
        String topic  = UUID.randomUUID().toString().replace("-", ""); //"PERSONS_TOPIC2";
        System.out.println("Topic: " + topic);
        DataStream<Person> expectedStream = PersonProvider.randomPersons();
        Iterator<Person> expectedIterator = DataStreamUtils.collect(expectedStream);
        FlinkKafkaProducer011<Person> personsKafkaProducer = new FlinkKafkaProducer011<Person>(topic,
                serializer, getProps(kafka)); // Semantic.EXACTLY_ONCE
        
        expectedStream.addSink(personsKafkaProducer);
        personsKafkaProducer.close();

        FlinkKafkaConsumer011<Person> personsKafkaConsumer = new FlinkKafkaConsumer011<Person>(
            topic, deserializer, getProps(kafka));
        personsKafkaConsumer.setStartFromEarliest();
        DataStream<Person> inputStream = expectedStream.getExecutionEnvironment().addSource(personsKafkaConsumer);
        Iterator<Person> inputIterator = DataStreamUtils.collect(inputStream);
        
        ArrayList<Person> expected = new ArrayList<>();
        DataStreamUtils.collect(expectedStream).forEachRemaining(expected::add);
        ArrayList<Person> received = new ArrayList<>();
        
        while (inputIterator.hasNext() && received.size() < expected.size()) {
            received.add(inputIterator.next());
            System.out.println(received.get(received.size() - 1));
        }

        Comparator<Person> compareFunction = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                if (o1.getAge() != o2.getAge()) {
                    return new Long(o1.getAge()).compareTo(new Long(o2.getAge()));
                }
                return o1.getName().compareTo(o2.getName());
            }
        };

        Assert.assertEquals(received.size(), expected.size());
        Collections.sort(received, compareFunction);
        Collections.sort(expected, compareFunction);
        for (int i = 0; i < expected.size(); i++) {
            // Assert.assertEquals(received.get(i).getName(), expected.get(i).getName());
            // Assert.assertEquals(received.get(i).getAge(), expected.get(i).getAge());
            // Assert.assertTrue(received.contains(expected.get(i)));
        }
        // Assert.assertEquals(received, expected);
        kafka.stop();
    }
}