package com.romnn.flinkkafkaprotobuf;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.ListTypeInfo;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import java.util.List;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.io.InputStream;
import java.lang.Exception;
import com.romnn.flinkkafkaprotobuf.protos.PersonProto.Person;


public class PersonDeserializer implements DeserializationSchema<Person> {

    @Override
    public Person deserialize(byte[] message) throws IOException {

        System.out.println("Got a message");
        try {
            Person p = Person.parseFrom(message);
            System.out.println("Parsed message");
            System.out.println(p);
            return p;
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new IOException("Unable to deserialize bytes");
        }
        /*
        try {
            return Person.parseFrom(message);
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new DeserializationException("Unable to deserialize bytes");
        }
        InputStream inputStream = new ByteArrayInputStream(message);
        while (true) {
            Person event = Person.parseDelimitedFrom(inputStream);
            if (event != null) {
                return event;
                // events.add(event);
            } else {
                break;
            }
        }
        return null;
        // return events;
        */
    }

    @Override
    public boolean isEndOfStream(Person nextElement) {
        return false;
    }

    @Override
    public TypeInformation<Person> getProducedType() {
        // return new ProtobufTypeInfo(Person.class);
        return TypeInformation.of(Person.class);
        // return new ListTypeInfo<>(LiveTrainData.class);
    }
}