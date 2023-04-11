package com.code.snippets.test.mqtt.controller;

import static java.time.LocalDateTime.now;

@Slf4j
@RestController
@RequestMapping("mqtt")
@RequiredArgsConstructor
public class MqttController {
    private static final AWSIotQos QOS = QOS1;

    private static final String DEFAULT_MESSAGE = "DEFAULT_MESSAGE";

    @Value("${cloud.aws.iot.default-topic}")
    private String DEFAULT_TOPIC;

    private final AWSIotMqttClient awsIotMqttClient;

    @GetMapping("/pub")
    public ResponseEntity<?> mqttPub(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String message) throws AWSIotException {

        topic = isEmpty(topic) ? DEFAULT_TOPIC : topic.trim();
        message = isEmpty(message) ? DEFAULT_MESSAGE : message.trim();

        JSONObject jsonData = new JSONObject()
                .appendField("topic", topic)
                .appendField("message", message)
                .appendField("publishedAt", now().toString());

        awsIotMqttClient.publish(topic, QOS, jsonData.toJSONString());

        return ok().body(new JSONObject()
                .appendField("response", jsonData)
                .appendField("status", SC_OK));
    }

    @GetMapping("/sub")
    public ResponseEntity<?> mqttSub(@RequestParam(required = false) String topic, @NotNull AWSIotQos qos) {

        topic = isEmpty(topic) ? DEFAULT_TOPIC : topic.trim();

        try {
            if (awsIotMqttClient == null) {

                log.warn("AWS IOT CLIENT IS NULL.");

                return badRequest().build();
            } else {

                awsIotMqttClient.subscribe(new MqttSubscribeHandler(topic, QOS));

                return ok().body(new JSONObject()
                        .appendField("response", new JSONObject()
                                .appendField("topic", topic)
                                .appendField("qos", qos))
                        .appendField("status", SC_OK));
            }

        } catch (Exception e) {
            log.error("ERROR OCCURRED :: {}", e.getMessage());
            e.printStackTrace();

            return badRequest().build();
        }
    }
}