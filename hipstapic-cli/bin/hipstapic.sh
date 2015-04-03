#!/bin/bash
TARGET_DIR=$(cd "$(dirname "$0")" && pwd)/../target
java -jar $TARGET_DIR/hipstapic-cli-*-jar-with-dependencies.jar $@