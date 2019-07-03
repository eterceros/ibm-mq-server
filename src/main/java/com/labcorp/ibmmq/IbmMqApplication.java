package com.labcorp.ibmmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@EnableJms
public class IbmMqApplication {

    private static final String DESTINATION_QUEUE = "DEV.QUEUE.1";
    private static final String MIN_KEY = "min";
    private static final String MAX_KEY = "max";
    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        SpringApplication.run(IbmMqApplication.class, args);
    }

    @GetMapping("send")
    String send() {
        try {
            Map<String, Integer> lpidChangeMap = generateMinMax();
            String message = String.format("oldLPID:%s newLPID:%s", lpidChangeMap.get(MIN_KEY).toString(),
                    lpidChangeMap.get(MAX_KEY).toString());
            jmsTemplate.convertAndSend(DESTINATION_QUEUE, message);
            return "OK";
        } catch (JmsException ex) {
            ex.printStackTrace();
            return "FAIL";
        }
    }

    private Map<String, Integer> generateMinMax() {
        int random1 = generateRandom();
        int random2 = generateRandom();
        Map<String, Integer> lpidMap = new HashMap<>();
        lpidMap.put(MIN_KEY, Math.min(random1, random2));
        lpidMap.put(MAX_KEY, Math.max(random1, random2));
        return lpidMap;
    }

    private Integer generateRandom() {
        int min = 1000;
        int max = 10000;
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
