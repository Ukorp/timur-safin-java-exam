package ru.mai.examine.rpks.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.kafka")
public class KafkaConfiguration {

  private String bootstrapServers;
  private Topics topics = new Topics();
  private Consumer consumer = new Consumer();
  private int fetchMaxSize;

  @Data
  public static class Topics {
    private String in;
    private String out;
  }

  @Data
  public static class Consumer {
    private String groupId;
    private String autoOffsetReset;
  }
}
