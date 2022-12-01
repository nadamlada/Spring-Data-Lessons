package hiberspring.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hiberspring.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfiguration {


    //TODO
    @Bean
    public Gson gson() {
        return null;
    }

    @Bean
    public ValidationUtil validationUtil() {
        return null;
    }

    @Bean
    public ModelMapper modelMapper() {
        return null;
    }
}
