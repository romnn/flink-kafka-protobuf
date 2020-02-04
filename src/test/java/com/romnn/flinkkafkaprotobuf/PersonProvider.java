package com.romnn.flinkkafkaprotobuf;

import java.util.ArrayList;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.testng.annotations.DataProvider;
import com.romnn.flinkkafkaprotobuf.protos.PersonProto.Person;

public class PersonProvider {

  public static PersonProvider getDefaultPerson() {
    Person.Builder builder = Person.newBuilder();
    builder.setName("Roman");
    return builder.build();
  }

  @DataProvider(name = "person-provider")
  public static DataStream<Person> weatherJoinOneLiveTrain() {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);
    ArrayList<Person> persons = new ArrayList<>() {
        add(this.getDefaultPerson());
    };

    DataStream<Person> personsStream = env.fromCollection(persons);
    /*
        .assignTimestampsAndWatermarks(
            new AscendingTimestampExtractor<LiveTrainData>() {
              @Override
              public long extractAscendingTimestamp(LiveTrainData liveTrainData) {
                return (long) liveTrainData.getMessageCreation();
              }
        });
    */
    personsStream.print();
    return personsStream
  }
















/*
  @DataProvider(name = "several-matching-live-train-weather-data-provider")
  public  static Object[][] weatherJoinSeveralLiveTrain(){
    StreamExecutionEnvironment env;
    env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);

    matchingTrains.add(trainEventWithLocationID(1));
    matchingTrains.add(trainEventWithLocationID(2));
    matchingTrains.add(trainEventWithLocationID(3));
    matchingTrains.add(trainEventWithLocationID(4));
    DataStream<LiveTrainData> liveTrainStream= env.fromCollection(matchingTrains)
        .assignTimestampsAndWatermarks(
            new AscendingTimestampExtractor<LiveTrainData>() {
              @Override
              public long extractAscendingTimestamp(LiveTrainData liveTrainData) {
                return (long) liveTrainData.getMessageCreation();
              }
            });
    ArrayList<Tuple2<WeatherData, Integer>> weather = new ArrayList<>();

    weather.add(correlatedWeatherEventWithLocationIDClass(1, "Clear_night"));
    weather.add(correlatedWeatherEventWithLocationIDClass(2, "Clear_night"));
    weather.add(correlatedWeatherEventWithLocationIDClass(3, "Clear_night"));
    weather.add(correlatedWeatherEventWithLocationIDClass(4, "Clear_night"));
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
    DataStream<Tuple2<WeatherData, Integer>> weatherStream = env.fromCollection(weather)
        .assignTimestampsAndWatermarks(
            new AscendingTimestampExtractor<Tuple2<WeatherData, Integer>>() {
              @Override
              public long extractAscendingTimestamp(
                  Tuple2<WeatherData, Integer> weatherDataIntegerTuple2) {
                return (long) weatherDataIntegerTuple2.f0.getStarttimestamp();
              }
            });
    return new Object[][] { {liveTrainStream, weatherStream} };
  }

  @DataProvider(name = "not-matching-live-train-weather-data-provider")
  public  static Object[][] weatherJoinNotMatchingLiveTrain(){
    StreamExecutionEnvironment env;
    env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);
    ArrayList<LiveTrainData> oneMatchingTrain = new ArrayList<>();

    oneMatchingTrain.add(trainEventWithLocationID(1));
    DataStream<LiveTrainData> liveTrainStream= env.fromCollection(oneMatchingTrain)
        .assignTimestampsAndWatermarks(
            new AscendingTimestampExtractor<LiveTrainData>() {
              @Override
              public long extractAscendingTimestamp(LiveTrainData liveTrainData) {
                return (long) liveTrainData.getMessageCreation();
              }
            });
    ArrayList<Tuple2<WeatherData, Integer>> weather = new ArrayList<>();

    weather.add(correlatedWeatherEventWithLocationIDClass(2, "weather1"));
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
    DataStream<Tuple2<WeatherData, Integer>> weatherStream = env.fromCollection(weather)
        .assignTimestampsAndWatermarks(
            new AscendingTimestampExtractor<Tuple2<WeatherData, Integer>>() {
              @Override
              public long extractAscendingTimestamp(
                  Tuple2<WeatherData, Integer> weatherDataIntegerTuple2) {
                return (long) weatherDataIntegerTuple2.f0.getStarttimestamp();
              }
            });
    liveTrainStream.print();
    weatherStream.print();
    return new Object[][] { {liveTrainStream, weatherStream} };
  }

  private static LiveTrainData trainEventWithLocationID(int locationId){
    return LiveTrainDataProvider.getDefaultLiveTrainDataEvent().toBuilder()
      .setLocationId(locationId).build();
  }

  private static Tuple2<WeatherData, Integer> correlatedWeatherEventWithLocationIDClass(int locationId, String eventClass){
    WeatherData weather = WeatherDataProvider.getDefaultWeatherEvent().toBuilder()
      .setEventclass(eventClass).build();
    return new Tuple2<>(weather, locationId);
  }
  */

}
