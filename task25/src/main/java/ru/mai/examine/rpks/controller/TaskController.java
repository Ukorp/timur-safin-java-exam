package ru.mai.examine.rpks.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.mai.examine.rpks.config.KafkaConfiguration;
import ru.mai.examine.rpks.config.MongoConfiguration;
import ru.mai.examine.rpks.kafka.KafkaReader;
import ru.mai.examine.rpks.service.MongoService;

import java.time.Duration;
import java.util.Objects;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TaskController {

  private final KafkaConfiguration kafkaConfiguration;
  private final MongoConfiguration mongoConfiguration;
  private final MongoService mongoService;

  @GetMapping("/task")
  @ResponseStatus(value = HttpStatus.OK)
  public String example(
          @RequestParam String bootstrapServers,
          @RequestParam String topic
  ) {

    KafkaConfig(bootstrapServers, topic);
    MongoCollection<Document> collection = getDocumentMongoCollection();
    KafkaReader kafkaReader = new KafkaReader(kafkaConfiguration);
    KafkaConsumer<String, String> consumer = kafkaReader.getConsumer();
    return mongoService.processing(consumer, collection);
  }

  private @NotNull MongoCollection<Document> getDocumentMongoCollection() {
    MongoClient mongoClient = MongoClients.create(mongoConfiguration.getConnectionString());
    MongoDatabase database = mongoClient.getDatabase(mongoConfiguration.getDatabase());
    MongoCollection<Document> collection = database.getCollection(mongoConfiguration.getCollection());
    return collection;
  }

  private void KafkaConfig(String bootstrapServers, String topic) {
    kafkaConfiguration.setBootstrapServers(bootstrapServers);
    kafkaConfiguration.getTopics().setIn(topic);
  }
}
