#!/usr/bin/env sh

DIRNAME=$(dirname "$0")
cd "$DIRNAME" || exit 1

# 使用 gradle wrapper jar
APP_BASE_NAME=$(basename "$0")
APP_HOME=$(cd "$(dirname "$0")" && pwd)
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

# 设置 JAVA_HOME（如果没有的话）
if [ -z "$JAVA_HOME" ]; then
    JAVA_HOME=$(/usr/libexec/java_home 2>/dev/null)
fi

exec java -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"