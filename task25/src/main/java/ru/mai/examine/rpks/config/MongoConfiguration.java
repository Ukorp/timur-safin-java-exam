package ru.mai.examine.rpks.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.mongo")
public class MongoConfiguration {

  private String connectionString;
  private String database;
  private String collection;

}
