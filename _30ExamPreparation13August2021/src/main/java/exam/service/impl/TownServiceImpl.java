package exam.service.impl;

import exam.model.dto.fromXml.towns.TownSeedRootDto;
import exam.model.entity.Town;
import exam.repository.TownRepository;
import exam.service.TownService;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static exam.paths.MyPaths.TOWNS_FILES_PATH;

@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    @Autowired
    public TownServiceImpl(TownRepository townRepository,
                           ValidationUtil validationUtil,
                           ModelMapper modelMapper,
                           XmlParser xmlParser) {
        this.townRepository = townRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(
                Path.of(TOWNS_FILES_PATH)
        );
    }

    @Override
    public String importTowns() throws JAXBException, FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();
        TownSeedRootDto townSeedRootDto = xmlParser
                .fromFile(TOWNS_FILES_PATH, TownSeedRootDto.class);

        townSeedRootDto
                .getTowns()
                .forEach(townSeedDto -> {
                    boolean valid = validationUtil.isValid(townSeedDto);

                    Town exist = townRepository.getTownByName(
                            townSeedDto.getName()
                    );

                    if (valid && exist == null) {
                        Town town = modelMapper.map(townSeedDto, Town.class);

                        stringBuilder.append(String.format(
                                "Successfully imported Town %s",
                                townSeedDto.getName()
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
    public Town getTownByName(String name) {
        return townRepository.getTownByName(name);
    }
}


