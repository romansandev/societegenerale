package com.example.exercise.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocieteGeneraleConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
