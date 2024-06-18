package net.wanji.business.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;

/**
 * @author: guanyuduo
 * @date: 2023/12/8 14:04
 * @descriptoin:
 */

public class KafkaListener implements Listener {

    private KafkaConsumer<String, String> consumer;
    private String topic;
    private boolean isRunning;

    public KafkaListener(KafkaConsumer<String, String> consumer, String topic) {
        this.consumer = consumer;
        this.topic = topic;
    }

    @Override
    public void open() {
        isRunning = true;
        // 创建一个新线程来进行监听
        Thread listenerThread = new Thread(() -> {
            while (isRunning) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    // 处理接收到的记录
                    System.out.println("Received message: " + record.value());
                }
            }
            consumer.close();
        });
        listenerThread.start();
    }

    @Override
    public void down() {
        isRunning = false;
    }
}
