# Start from Zulu OpenJDK 17
FROM azul/zulu-openjdk:17

# Scala version
ENV SCALA_VERSION 2.13.13

# sbt version
ENV SBT_VERSION 1.9.9

# Install sbt
RUN \
  apt-get update && \
  apt-get install -y curl && \
  curl -L "https://github.com/sbt/sbt/releases/download/v$SBT_VERSION/sbt-$SBT_VERSION.tgz" | tar zx -C /usr/local --strip-components=1


WORKDIR /app

COPY project /app/project
COPY build.sbt /app/

COPY src /app/src

# Build the application using sbt
RUN sbt clean compile

# Specify the command to run
CMD ["sbt", "run"]
