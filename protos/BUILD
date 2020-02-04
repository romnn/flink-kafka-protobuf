load("@rules_proto//proto:defs.bzl", "proto_library")
load("@rules_java//java:defs.bzl", "java_proto_library")

proto_library(
    name = "person_proto",
    srcs = ["person.proto"],
    visibility = ["//visibility:public"],
)

java_proto_library(
    name = "person_java_proto",
    deps = [":person_proto"],
    visibility = ["//visibility:public"],
)