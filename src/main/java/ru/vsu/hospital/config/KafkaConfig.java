package ru.vsu.hospital.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String DOCTORS_TOPIC = "hospital.doctors";
    public static final String PATIENTS_TOPIC = "hospital.patients";
    public static final String MEDICAL_CARDS_TOPIC = "hospital.medical-cards";

    @Bean
    public NewTopic doctorsTopic() {
        return TopicBuilder.name(DOCTORS_TOPIC).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic patientsTopic() {
        return TopicBuilder.name(PATIENTS_TOPIC).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic medicalCardsTopic() {
        return TopicBuilder.name(MEDICAL_CARDS_TOPIC).partitions(1).replicas(1).build();
    }
}
