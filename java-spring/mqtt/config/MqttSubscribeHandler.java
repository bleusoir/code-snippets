package com.code.snippets.test.mqtt.config;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import lombok.extern.slf4j.Slf4j;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class MqttSubscribeHandler extends AWSIotTopic {

    @Override
    public void onMessage(AWSIotMessage message) {
        log.info("TOPIC :: {}, QOS :: {}, MESSAGE :: {}",
                message.getTopic(), message.getQos(), new String(message.getPayload(), UTF_8));
    }

    public MqttSubscribeHandler(String topic, AWSIotQos qos) {
        super(topic, qos);
    }
}