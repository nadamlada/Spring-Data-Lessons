package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.fromXml.ForecastSeedRootDto;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.DayOfWeek;
import softuni.exam.models.entity.Forecast;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.CityService;
import softuni.exam.service.ForecastService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.Paths.Paths.FORECASTS_FILE_PATH;

@Service
public class ForecastServiceImpl implements ForecastService {
    private final ForecastRepository forecastRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    //
    private final CityService cityService;

    public ForecastServiceImpl(ForecastRepository forecastRepository,
                               ValidationUtil validationUtil,
                               ModelMapper modelMapper,
                               XmlParser xmlParser, CityService cityService) {
        this.forecastRepository = forecastRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.cityService = cityService;
    }


    @Override
    public boolean areImported() {
        return forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files.readString(
                Path.of(FORECASTS_FILE_PATH)
        );
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        StringBuilder stringBuilder = new StringBuilder();
        ForecastSeedRootDto forecastSeedRootDto = xmlParser
                .fromFile(FORECASTS_FILE_PATH, ForecastSeedRootDto.class);

        forecastSeedRootDto
                .getForecasts()
                .forEach(forecastSeedDto -> {
                    boolean valid = validationUtil.isValid(forecastSeedDto);

                    City city = cityService.getCityById(forecastSeedDto.getCity());

                    //проверка дали енъма не е нъл
                    if (forecastSeedDto.getDayOfWeek() == null || city == null) {
                        stringBuilder
                                .append("Invalid forecast")
                                .append(System.lineSeparator());
                    } else {
                        Forecast exists = forecastRepository
                                .getForecastByCityAndDayOfWeek(
                                        city,
                                        forecastSeedDto.getDayOfWeek()
                                );

                        if (valid && exists == null) {
                            Forecast forecast = modelMapper.map(
                                    forecastSeedDto, Forecast.class);

                            forecast.setCity(city);

                            stringBuilder.append(String.format(
                                    "Successfully import forecast %s - %.2f",
                                    forecastSeedDto.getDayOfWeek().toString(),
                                    forecastSeedDto.getMaxTemperature()
                            ));

                            forecastRepository.saveAndFlush(forecast);
                        } else {
                            stringBuilder.append("Invalid forecast");
                        }

                        stringBuilder.append(System.lineSeparator());
                    }
                });

        return stringBuilder.toString().trim();
    }

    @Override
    public String exportForecasts() {
        StringBuilder stringBuilder = new StringBuilder();
        forecastRepository
                .findAllByDayOfWeekAndCity_PopulationLessThanOrderByMaxTemperatureDescIdAsc(
                        DayOfWeek.SUNDAY, 150000)
                .forEach(forecast -> {
                    stringBuilder.append(String.format(
                            """
                                    City: %s:
                                    -min temperature: %.2f
                                    --max temperature: %.2f
                                    ---sunrise: %s
                                    ----sunset: %s
                                    """,
                            forecast.getCity().getCityName(),
                            forecast.getMinTemperature(),
                            forecast.getMaxTemperature(),
                            forecast.getSunrise().toString(),
                            forecast.getSunset().toString()
                    ));
                });

        return stringBuilder.toString().trim();
    }
}

