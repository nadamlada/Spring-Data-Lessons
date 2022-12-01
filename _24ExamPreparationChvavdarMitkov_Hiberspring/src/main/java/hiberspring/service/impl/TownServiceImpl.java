package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dto.fromJson.TownSeedDto;
import hiberspring.domain.entity.Town;
import hiberspring.repository.TownRepository;
import hiberspring.service.TownService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static hiberspring.common.GlobalConstants.*;

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
    public Boolean townsAreImported() {
        return townRepository.count() > 0;
    }

    @Override
    public String readTownsJsonFile() throws IOException {
        return Files.readString(
                Path.of(TOWNS_FILE_PATH)
        );
    }

    @Override
    public String importTowns(String townsFileContent) throws IOException {
        //ToDo не работи
        //https://youtu.be/Yh0WwmFBFM4
        StringBuilder stringBuilder = new StringBuilder();

        TownSeedDto[] townSeedDtos = gson
                .fromJson(readTownsJsonFile(), TownSeedDto[].class);

        Arrays.stream(townSeedDtos)
                .forEach(townSeedDto -> {
                    boolean isValid = validationUtil.isValid(townSeedDto);
                    //проверка дали го нямаме в репозиторито вече вкаран този град
                    Town exists = townRepository.findByName(townSeedDto.getName());

                    if (isValid && exists == null) {
                        Town town = modelMapper.map(townSeedDto, Town.class);

                        stringBuilder.append(
                                String.format(SUCCESSFUL_IMPORT_MESSAGE,
                                        "Town",
                                        townSeedDto, getClass().getName()
                                )
                        );

                        townRepository.saveAndFlush(town);
                    } else {
                        stringBuilder.append(INCORRECT_DATA_MESSAGE);
                    }

                    stringBuilder.append(System.lineSeparator());

                });

        return stringBuilder.toString().trim();
    }

    @Override
    public Town getTownByName(String name) {
        return townRepository.findByName(name);
    }
}
