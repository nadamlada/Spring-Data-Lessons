package softuni.exam.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.ValidatorUtilImpl;
import softuni.exam.util.XmlParser;
import softuni.exam.util.XmlParserImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public ValidatorUtil validationUtil() {
        return new ValidatorUtilImpl();
    }

//    @Bean
//    public ModelMapper modelMapper() {
//        return new ModelMapper();
//    }

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

        return modelMapper;
    }

    @Bean
    public XmlParser xmlParser (){
        return new XmlParserImpl();
    }
}
