package com.labcorp.ibmmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableJms
public class IbmMqApplication {

    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        SpringApplication.run(IbmMqApplication.class, args);
    }

    @GetMapping("send")
    String send() {
        try {
            jmsTemplate.convertAndSend("DEV.QUEUE.1", "Hello World!");
            return "OK";
        } catch (JmsException ex) {
            ex.printStackTrace();
            return "FAIL";
        }
    }
    //call lpid update

    @GetMapping("recv")
    String recv() {
        try {
            return jmsTemplate.receiveAndConvert("DEV.QUEUE.1").toString();
        } catch (JmsException ex) {
            ex.printStackTrace();
            return "FAIL";
        }
    }

    @JmsListener(destination = "DEV.QUEUE.1")
    public void handleMessage(String message) {//implicit message type conversion
        System.out.println("received: " + message);
        //todo see what happens if in the middle the sender goes down and if it goes alive again check if listener
        // still works
    }
}
