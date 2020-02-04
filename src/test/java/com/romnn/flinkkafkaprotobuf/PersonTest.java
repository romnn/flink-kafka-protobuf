package com.romnn.flinkkafkaprotobuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import javax.xml.crypto.Data;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamUtils;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.bptlab.cepta.config.PostgresConfig;
import org.bptlab.cepta.operators.WeatherLiveTrainJoinFunction;
import org.bptlab.cepta.operators.WeatherLocationCorrelationFunction;
import org.junit.Before;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.romnn.flinkkafkaprotobuf.PersonProvider;
import com.romnn.flinkkafkaprotobuf.PersonSerializer;
import com.romnn.flinkkafkaprotobuf.protos.PersonProto.Person;

public class PersonTests {

  @Test(groups = {"unit-tests"},
      dataProvider = "person-provider",
      dataProviderClass = PersonProvider.class)
  public void testSerialization(DataStream<Person> inputStream) throws Exception {

    FlinkKafkaProducer011<Person> personsKafkaProducer =
      new FlinkKafkaProducer011<Person>("PERSONS_TOPIC", new PersonSerializer(), properties);

    inputStream.addSink(personsKafkaProducer);
  }
}