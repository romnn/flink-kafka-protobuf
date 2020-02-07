package com.romnn.flinkkafkaprotobuf;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.typeutils.ListTypeInfo;
import org.apache.flink.api.common.serialization.SerializationSchema;
import com.google.protobuf.Message;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Parser;
import java.util.List;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.Exception;
import java.lang.reflect.Method;
import java.lang.annotation.Target;

import com.romnn.flinkkafkaprotobuf.protos.PersonProto.Person;

public class GenericBinaryProtoSerializer<T extends GeneratedMessageV3> implements SerializationSchema<T> {

  @Override
    public byte[] serialize(T value) {
        return value.toByteArray();
    }
}