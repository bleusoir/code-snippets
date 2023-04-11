package com.code.snippets.test.mqtt.config;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.auth.Credentials;
import com.amazonaws.services.iot.client.auth.StaticCredentialsProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

@Slf4j
@Configuration
public class MqttAwsIotConfig {

    @Value("${cloud.aws.region.static}")
    private Region REGION;

    @Value("${cloud.aws.credentials.access-key}")
    private String ACCESS_KEY;

    @Value("${cloud.aws.credentials.secret-key}")
    private String SECRET_KEY;

    @Value("${cloud.aws.iot.endpoint}")
    private String ENDPOINT;

    @Value("${cloud.aws.iot.client-id}")
    private String CLIENT_ID;

    @Bean
    public AWSIotMqttClient buildAwsIotMqttClient() throws Exception {
        StaticCredentialsProvider staticProvider = new StaticCredentialsProvider(new Credentials(ACCESS_KEY, SECRET_KEY));

        AWSIotMqttClient client = new AWSIotMqttClient(ENDPOINT, CLIENT_ID, staticProvider, REGION.id());

        try {

            client.connect();
            return client;

        } catch (AWSIotException e) {
            log.error("ERROR OCCURRED :: {}", e.getMessage());
            e.printStackTrace();

            throw new Exception(e.getMessage());
        }
    }
}