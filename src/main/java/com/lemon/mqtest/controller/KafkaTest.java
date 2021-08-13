package com.lemon.mqtest.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@RestController
public class KafkaTest {
    @Autowired
    private KafkaTemplate<Integer, String> template;


    @GetMapping("")
    public Object test(){
        String msg = "msgbody";

        final ListenableFuture<SendResult<Integer, String>> future = template.send("lemon_mq_test", msg);
        try {
            final SendResult<Integer, String> sendResult = future.get();
            return sendResult.toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "nothing";
    }
    @KafkaListener(topics = "lemon_mq_test",groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<Integer, String> record){
        System.out.println(record);
    }
}
