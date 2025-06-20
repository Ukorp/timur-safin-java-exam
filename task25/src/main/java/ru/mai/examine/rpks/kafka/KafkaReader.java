package ru.mai.examine.rpks.kafka;

import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import ru.mai.examine.rpks.config.KafkaConfiguration;

import java.util.Collections;
import java.util.Properties;

public class KafkaReader {

  @Getter
  KafkaConsumer<String, String> consumer;
  KafkaConfiguration config;

  public KafkaReader(KafkaConfiguration config) {
    this.config = config;

    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
    props.put(ConsumerConfig.GROUP_ID_CONFIG, config.getConsumer().getGroupId());
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, config.getConsumer().getAutoOffsetReset());
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    this.consumer = new KafkaConsumer<>(props);
    String topic = config.getTopics().getIn();
    this.consumer.subscribe(Collections.singletonList(topic));
  }

}
