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
        try {
            return Person.parseFrom(message);
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new IOException("Unable to deserialize bytes");
        }
    }

    @Override
    public boolean isEndOfStream(Person nextElement) {
        return false;
    }

    @Override
    public TypeInformation<Person> getProducedType() {
        return TypeInformation.of(Person.class);
    }
}