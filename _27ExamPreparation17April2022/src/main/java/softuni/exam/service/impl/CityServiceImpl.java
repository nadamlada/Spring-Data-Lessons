package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.fromJson.CityRootDto;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CityRepository;
import softuni.exam.service.CityService;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static softuni.exam.Paths.Paths.CITIES_FILE_PATH;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    //(cascade = CascadeType.MERGE)
    private final CountryService countryService;

    public CityServiceImpl(CityRepository cityRepository,
                           ValidationUtil validationUtil,
                           ModelMapper modelMapper,
                           Gson gson,
                           CountryService countryService) {
        this.cityRepository = cityRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.countryService = countryService;
    }

    @Override
    public boolean areImported() {
        return cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return Files.readString(
                Path.of(CITIES_FILE_PATH)
        );
    }

    @Override
    public String importCities() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        CityRootDto[] cityRootDtos = gson
                .fromJson(readCitiesFileContent(), CityRootDto[].class);

        System.out.println();
        Arrays.stream(cityRootDtos)
                .forEach(cityRootDto -> {
                    boolean valid = validationUtil.isValid(cityRootDto);
                    City exists = cityRepository
                            .getCityByCityName(cityRootDto.getCityName());
                    Country country = countryService
                            .getCountryById(cityRootDto.getCountry());

                    if (valid && exists == null) {
                        City city = modelMapper.map(cityRootDto, City.class);
                        city.setCountry(country);

                        stringBuilder.append(String.format(
                                "Successfully imported city %s - %d",
                                cityRootDto.getCityName(),
                                cityRootDto.getPopulation()
                        ));

                        cityRepository.saveAndFlush(city);
                    } else {
                        stringBuilder.append("Invalid city");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }

    @Override
    public City getCityById(Long id) {
        return cityRepository.getCityById(id);
    }
}
