load("@rules_java//java:defs.bzl", "java_test", "java_library")

java_library(
    name = "example-testlib",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = [
        "//:example",
        "//protos:person_java_proto",
        "@maven//:junit_junit_4_13",
        "@maven//:org_testcontainers_testcontainers",
        "@maven//:org_testcontainers_kafka",
        "@maven//:org_apache_flink_flink_core",
        "@maven//:org_apache_flink_flink_java",
        "@maven//:org_apache_flink_flink_streaming_java_2_11",
        "@maven//:org_apache_flink_flink_connector_kafka_0_11_2_11",
    ],
)

java_test(
    name = "integration",
    srcs = ["PersonIntegrationTest.java"],
    test_class = "com.romnn.flinkkafkaprotobuf.PersonIntegrationTest",
    deps = [
        # "//examples/java-native/src/main/java/com/example/myproject:custom-greeting",
        # "//third_party:junit4",
        "//protos:person_java_proto",
        "//:example",
        ":example-testlib",
        "@maven//:org_apache_flink_flink_streaming_java_2_11",
        "@maven//:org_testcontainers_kafka",
        "@maven//:org_apache_flink_flink_connector_kafka_0_11_2_11",
        "@maven//:org_apache_flink_flink_core",
        # "@maven//:org_apache_flink_flink_connector_kafka",
        # "//:example-lib",
        # "@maven//:junit_junit_4_13",
        # "@maven//:org_testcontainers_testcontainers",
        # "@maven//:org_testcontainers_kafka",
    ],
)