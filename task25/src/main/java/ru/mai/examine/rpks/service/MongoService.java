package ru.mai.examine.rpks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class MongoService {

    private final ObjectMapper objectMapper;

    public String processing(KafkaConsumer<String, String> consumer, MongoCollection<Document> collection) {
        while (true) {
            try {
                var msg = consumer.poll(Duration.ofSeconds(1));
                for (var elem : msg) {
                    String receivedMessage = elem.value();
                    JsonNode node;
                    try {
                        node = objectMapper.readTree(receivedMessage);
                    } catch (JsonProcessingException e) {
                        continue;
                    }
                    String res = node.get("test").asText();
                    if (res != null) {
                        var founded = collection.find(new Document("test_field", res));
                        if (!Objects.requireNonNull(founded.first()).isEmpty()) {
                            var ans =  founded.first().get("test_value_field").toString();
                            if (ans != null) {
                                return ans;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                consumer.close();
            }
        }
    }

}
