load("@rules_java//java:defs.bzl", "java_test", "java_binary", "java_library")

java_binary(
    name = "example",
    srcs = glob(["src/main/java/**/*.java"]),
    resources = glob([
        "src/main/resources/**",
    ]),
    main_class = "com.romnn.flinkkafkaprotobuf.Main",
    visibility = ["//visibility:public"],
    deps = [
        "//protos:person_java_proto",
        # "@maven//:commons_io_commons_io_2_6",
        # "@maven//:joda_time_joda_time_2_9_7",
        # "@maven//:info_picocli_picocli",
        # "@maven//:com_twitter_chill_protobuf",
        "@maven//:com_google_protobuf_protobuf_java",
        "@maven//:org_testcontainers_testcontainers",
        "@maven//:org_testcontainers_kafka",
        # "@maven//:org_apache_commons_commons_lang3",
        "@maven//:org_apache_flink_flink_core",
        "@maven//:org_apache_flink_flink_java",
        # "@maven//:org_apache_flink_flink_avro",
        # "@maven//:org_apache_avro_avro",
        "@maven//:org_apache_flink_flink_streaming_java_2_11",
        "@maven//:org_apache_flink_flink_connector_kafka_0_11_2_11",
        "@maven//:org_apache_kafka_kafka_clients",
        "@maven//:org_slf4j_slf4j_log4j12",
        "@maven//:org_slf4j_slf4j_api",
        # "@maven//:com_github_jasync_sql_jasync_postgresql",
        # "@maven//:com_github_jasync_sql_jasync_common",
    ],
)

java_library(
    name = "example-lib",
    srcs = glob(["src/main/java/**/*.java"]),
    visibility = ["//visibility:public"],
    deps = [
        "//protos:person_java_proto",
        "@maven//:com_google_protobuf_protobuf_java",
        "@maven//:org_apache_flink_flink_core",
        "@maven//:org_apache_flink_flink_java",
        "@maven//:org_apache_flink_flink_streaming_java_2_11",
        "@maven//:org_apache_flink_flink_connector_kafka_0_11_2_11",
        "@maven//:org_apache_kafka_kafka_clients",
        "@maven//:org_slf4j_slf4j_log4j12",
        "@maven//:org_slf4j_slf4j_api",
    ],
)

java_library(
    name = "example-testlib2",
    # srcs = glob(["src/test/java/**/*.java"]),
    visibility = ["//visibility:public"],
    runtime_deps = [
        "//protos:person_java_proto",
        ":example",
        "@maven//:org_testng_testng_7_1_0",
        "@maven//:junit_junit_4_13",
        "@maven//:org_testcontainers_testcontainers",
        "@maven//:org_testcontainers_kafka",
        "@maven//:org_apache_flink_flink_connector_kafka_0_11_2_11",
        "@maven//:org_apache_flink_flink_core",
        "@maven//:org_apache_flink_flink_java",
        "@maven//:org_apache_flink_flink_streaming_java_2_11",
    ],
)

java_test(
    name="all2",
    size="small",
    runtime_deps=[
        ":example",
        ":example-testlib",
        "@maven//:org_testng_testng_7_1_0",
    ],
    data=["src/test/testng.xml"],
    use_testrunner=False,
    main_class="org.testng.TestNG",
    args=["src/test/testng.xml"],
)