package softuni.exam.instagraphlite.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import softuni.exam.instagraphlite.util.ValidationUtil;
import softuni.exam.instagraphlite.util.XmlParser;
import softuni.exam.instagraphlite.util.impl.ValidationUtilImpl;
import softuni.exam.instagraphlite.util.impl.XmlParserImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }

    @Bean
    public ValidationUtil validationUtil() {
        return new ValidationUtilImpl();
    }

    /*
        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(
                new Converter<String, LocalDate>() {
                    @Override
                    public LocalDate convert(MappingContext<String, LocalDate> mappingContext) {
                        return LocalDate
                                .parse(mappingContext.getSource(),
                                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                );
                    }
                }
        );

        modelMapper.addConverter(
                new Converter<String, LocalDateTime>() {
                    @Override
                    public LocalDateTime convert(MappingContext<String, LocalDateTime> mappingContext) {
                        return LocalDateTime
                                .parse(mappingContext.getSource(),
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                );
                    }
                }
        );

        modelMapper.addConverter(
                new Converter<String, LocalTime>() {
                    @Override
                    public LocalTime convert(MappingContext<String, LocalTime> mappingContext) {
                        return LocalTime
                                .parse(mappingContext.getSource(),
                                        DateTimeFormatter.ofPattern("HH:mm:ss")
                                );
                    }
                }
        );

        return modelMapper;
    }

    @Bean
    public XmlParser xmlParser() {
        return new XmlParserImpl();
    }
}