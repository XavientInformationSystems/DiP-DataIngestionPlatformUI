package com.xavient.dataingest.ui;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaPublisher {

	public static boolean sendToKafka(String text) {
		Properties props = new Properties();
		props.put("metadata.broker.list", "10.5.3.166:6667");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		//props.put("partitioner.class", "SimplePartitioner");
		props.put("request.required.acks", "1");
		 ProducerConfig config = new ProducerConfig(props);

		Producer<String, String> producer = new Producer<String, String>(config);
		
		
		KeyedMessage<String, String> data = new KeyedMessage<String, String>("kafka_topic", text);
		producer.send(data);

		return true;

	}

}
