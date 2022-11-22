package com.example.football.service.impl;

import com.example.football.models.dto.FromJson.TownSeedDto;
import com.example.football.models.entity.Town;
import com.example.football.repository.TownRepository;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static com.example.football.paths.Paths.TOWNS_FILE_PATH;

@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    @Autowired
    public TownServiceImpl(TownRepository townRepository,
                           ValidationUtil validationUtil,
                           ModelMapper modelMapper,
                           Gson gson) {
        this.townRepository = townRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }


    @Override
    public boolean areImported() {
        return townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(
                Path.of(TOWNS_FILE_PATH)
        );
    }

    @Override
    public String importTowns() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        TownSeedDto[] townSeedDtos = gson
                .fromJson(readTownsFileContent(), TownSeedDto[].class);

        Arrays.stream(townSeedDtos)
                .forEach(townSeedDto -> {
                    boolean valid = validationUtil.isValid(townSeedDto);
                    Town exists = townRepository.getTownByName(
                            townSeedDto.getName()
                    );

                    if (valid && exists == null) {
                        Town town = modelMapper.map(townSeedDto, Town.class);

                        stringBuilder.append(String.format(
                                "Successfully imported Town %s - %d",
                                townSeedDto.getName(),
                                townSeedDto.getPopulation()
                        ));

                        townRepository.saveAndFlush(town);
                    } else {
                        stringBuilder.append("Invalid Town");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }

    @Override
    public Town getTownByTownName(String name) {
        return townRepository.getTownByName(name);
    }
}
