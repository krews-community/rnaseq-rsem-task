FROM openjdk:8-jre-slim as base
RUN apt-get update && apt-get install -y \
    build-essential \
    wget \
    unzip \
    python-pip \
    python3 \
    python3-pip \
    liblzma-dev \
    libbz2-dev \
    zlib1g-dev \
    python \
     git

RUN mkdir /software
WORKDIR /software
ENV PATH="/software:${PATH}"

RUN wget https://github.com/deweylab/RSEM/archive/v1.2.31.zip && \
    unzip v1.2.31.zip && rm v1.2.31.zip && \
    cd RSEM-1.2.31 && make

ENV PATH="/software/RSEM-1.2.31:${PATH}"


FROM openjdk:8-jdk-alpine as build
COPY . /src
WORKDIR /src

RUN ./gradlew clean shadowJar

FROM base
RUN mkdir /app
COPY --from=build /src/build/rnaseq-rsemquant-*.jar /app/rnaseq.jar