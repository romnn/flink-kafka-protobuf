package com.romnn.flinkkafkaprotobuf;

import org.apache.flink.api.common.serialization.SerializationSchema;
import com.romnn.flinkkafkaprotobuf.protos.PersonProto.Person;

public class PersonSerializer implements SerializationSchema<Person> {
    @Override
    public byte[] serialize(Person value) {
        return value.toByteArray();
    }
}