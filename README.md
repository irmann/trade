# Trade Store

This is a backend for an app to store some kind of trade in a database.

## Check Java version
java -version

java version "11.0.7" 2020-04-14 LTS
Java(TM) SE Runtime Environment 18.9 (build 11.0.7+8-LTS)
Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.7+8-LTS, mixed mode)

## Install Database and Kafka

### setup database
docker run --name some-postgres -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword -d postgres

### create table
docker exec -it some-postgres  bash

psql -h localhost -U postgres

CREATE DATABASE demo;

\c demo;

CREATE TABLE trade(id serial PRIMARY KEY,trade_id VARCHAR (20), trade_version integer, country_party_id VARCHAR (20), book_id VARCHAR (20), maturity_date timestamp, created_date timestamp, expired boolean);


### start the database
docker start some-postgres


### setup kafka 
wget https://downloads.apache.org/kafka/2.5.0/kafka_2.12-2.5.0.tgz
tar -xzf kafka_2.12-2.5.0.tgz
cd kafka_2.12-2.5.0
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties

#### create a topic
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test

bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test2

#### create a producer and a consumer
bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic test

bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test2 --from-beginning

## Build
gradle build

## Run
java -jar build/libs/trade-0.0.1-SNAPSHOT.jar

## Test

curl -X POST \
  http://localhost:8080/api/v1/trade \
  -H 'Content-Type: application/json' \
  -d '{
"tradeId": "T5",
"tradeVersion": "7",
"countryPartyId": "CP-2",
"bookId": "B2",
"maturityDate": "01/06/2020",
"createdDate": "01/06/2020",
"expired": "true"
}'

curl -X GET http://localhost:8080/api/v1/trade

copy and past to kafka console producer

{"tradeId": "T65", "tradeVersion": "5", "countryPartyId": "CP-20", "bookId": "B20", "maturityDate": "01/06/2020", "createdDate": "22/06/2020","expired": "false"}
