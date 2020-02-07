load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "rules_proto",
    sha256 = "602e7161d9195e50246177e7c55b2f39950a9cf7366f74ed5f22fd45750cd208",
    strip_prefix = "rules_proto-97d8af4dc474595af3900dd85cb3a29ad28cc313",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/rules_proto/archive/97d8af4dc474595af3900dd85cb3a29ad28cc313.tar.gz",
        "https://github.com/bazelbuild/rules_proto/archive/97d8af4dc474595af3900dd85cb3a29ad28cc313.tar.gz",
    ],
)
load("@rules_proto//proto:repositories.bzl", "rules_proto_dependencies", "rules_proto_toolchains")
rules_proto_dependencies()
rules_proto_toolchains()

FLINK_VERSION = "1.9.0"

SCALA_VERSION = "2.11"

RULES_JVM_EXTERNAL_TAG = "2.8"

RULES_JVM_EXTERNAL_SHA = "79c9850690d7614ecdb72d68394f994fef7534b292c4867ce5e7dec0aa7bdfad"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        # "org.apache.commons:commons-lang3:3.9",
        "org.testng:testng:7.1.0",
        "junit:junit:4.13",
        "org.testcontainers:testcontainers:1.12.5",
        "org.testcontainers:kafka:1.12.5",
        # "commons-io:commons-io:2.6",
        # "com.google.code.findbugs:jsr305:1.3.9",
        # "com.google.errorprone:error_prone_annotations:2.0.18",
        # "com.google.j2objc:j2objc-annotations:1.1",
        "com.google.protobuf:protobuf-java:3.11.1",
        # "info.picocli:picocli:4.1.0",
        "org.slf4j:slf4j-log4j12:1.7.5",
        "org.slf4j:slf4j-api:1.7.28",
        # "com.esotericsoftware:kryo:4.0.0",
        # "com.esotericsoftware:kryo:5.0.0-RC4",
        # "com.esotericsoftware.kryo:kryo:2.24.0",
        # "org.apache.avro:avro:1.9.0",
        # "com.twitter:chill-protobuf:0.9.5",
        # "com.github.jasync-sql:jasync-postgresql:1.0.11",
        # "com.github.jasync-sql:jasync-common:1.0.11",
        # "joda-time:joda-time:2.9.7",
        "org.apache.kafka:kafka-clients:2.4.0",
        "org.apache.flink:flink-core:%s" % FLINK_VERSION,
        "org.apache.flink:flink-java:%s" % FLINK_VERSION,
        # "org.apache.flink:flink-avro:%s" % FLINK_VERSION,
        "org.apache.flink:flink-streaming-java_%s:%s" % (SCALA_VERSION, FLINK_VERSION),
        "org.apache.flink:flink-connector-kafka-0.11_%s:%s" % (SCALA_VERSION, FLINK_VERSION),
    ],
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
)