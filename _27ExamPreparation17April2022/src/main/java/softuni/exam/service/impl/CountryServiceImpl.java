package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.fromJson.CountrySeedDto;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static softuni.exam.Paths.Paths.COUNTRIES_FILE_PATH;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public CountryServiceImpl(CountryRepository countryRepository,
                              ValidationUtil validationUtil,
                              ModelMapper modelMapper,
                              Gson gson) {
        this.countryRepository = countryRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return Files.readString(
                Path.of(COUNTRIES_FILE_PATH)
        );
    }

    @Override
    public String importCountries() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        CountrySeedDto[] countrySeedDtos = gson
                .fromJson(readCountriesFromFile(), CountrySeedDto[].class);

        Arrays.stream(countrySeedDtos)
                .forEach(countrySeedDto -> {
                    boolean valid = validationUtil.isValid(countrySeedDto);
                    Country exists = countryRepository
                            .getCountryByCountryName(countrySeedDto.getCountryName());

                    if (valid && exists == null) {
                        Country country = modelMapper.map(countrySeedDto, Country.class);

                        stringBuilder.append(String.format(
                                "Successfully imported country %s - %s",
                                countrySeedDto.getCountryName(),
                                countrySeedDto.getCurrency()
                        ));

                        countryRepository.saveAndFlush(country);
                    } else {
                        stringBuilder.append("Invalid country");
                    }

                    stringBuilder.append(System.lineSeparator());

                });

        return stringBuilder.toString().trim();
    }

    @Override
    public Country getCountryById(Long id) {
        return countryRepository.getCountryById(id);
    }
}