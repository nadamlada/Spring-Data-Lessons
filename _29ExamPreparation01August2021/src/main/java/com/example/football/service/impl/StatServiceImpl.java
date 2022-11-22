package com.example.football.service.impl;

import com.example.football.models.dto.fromXml.StatSeedRootDto;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.example.football.paths.Paths.STATS_FILE_PATH;

@Service
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    @Autowired
    public StatServiceImpl(StatRepository statRepository,
                           ValidationUtil validationUtil,
                           ModelMapper modelMapper,
                           XmlParser xmlParser) {
        this.statRepository = statRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }


    @Override
    public boolean areImported() {
        return statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return Files.readString(
                Path.of(STATS_FILE_PATH)
        );
    }

    @Override
    public String importStats() throws JAXBException, FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();

        StatSeedRootDto statSeedRootDto = xmlParser
                .fromFile(STATS_FILE_PATH, StatSeedRootDto.class);

            statSeedRootDto
                    .getStats()
                    .forEach(statSeedDto -> {
                        boolean valid = validationUtil.isValid(statSeedDto);
                        Stat exists = statRepository
                                .getByPassingAndEnduranceAndShooting(
                                statSeedDto.getPassing(),
                                statSeedDto.getEndurance(),
                                statSeedDto.getShooting()
                        );

                        if(valid && exists == null) {
                            Stat stat = modelMapper.map(statSeedDto, Stat.class);

                            stringBuilder.append(String.format(
                                    "Successfully imported Stat %.2f - %.2f - %.2f",
                                    statSeedDto.getShooting(),
                                    statSeedDto.getPassing(),
                                    statSeedDto.getEndurance()
                            ));

                            statRepository.saveAndFlush(stat);
                        } else {
                            stringBuilder.append("Invalid Stat");
                        }

                        stringBuilder.append(System.lineSeparator());
                    });

        return stringBuilder.toString().trim();
    }

    @Override
    public Stat getStatById(Long id) {
        return statRepository.getStatById(id);
    }
}
