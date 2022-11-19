package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.fromJson.TownSeedDto;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static softuni.Constants.Paths.TOWNS_FILE_PATH;

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
                    Town exists = townRepository.getByTownName(townSeedDto.getTownName());

                    if(valid && exists == null) {
                        Town town = modelMapper.map(townSeedDto, Town.class);

                        stringBuilder.append(String.format(
                                "Successfully imported town %s - %d",
                                townSeedDto.getTownName(),
                                townSeedDto.getPopulation()
                        ));

                        townRepository.saveAndFlush(town);
                    } else {
                        stringBuilder.append("Invalid town");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }

    @Override
    public Town getByName(String name) {
        return townRepository.getByTownName(name);
    }
}
