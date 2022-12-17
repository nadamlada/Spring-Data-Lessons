package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.fromXml.teams.TeamSeedRootDto;
import softuni.exam.domain.entity.Picture;
import softuni.exam.domain.entity.Team;
import softuni.exam.repository.TeamRepository;
import softuni.exam.service.PictureService;
import softuni.exam.service.TeamService;
import softuni.exam.util.impl.ValidationUtilImpl;
import softuni.exam.util.XmlParser;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Transactional
public class TeamServiceImpl implements TeamService {
    public static final String TEAMS_FILE_PATH =
            "src/main/resources/files/xml/teams.xml";

    private final TeamRepository teamRepository;
    private final ValidationUtilImpl validatorUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    //слагаме на Team entity на пикчъра cascade = CascadeType.PERSIST
    private final PictureService pictureService;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository,
                           ValidationUtilImpl validatorUtil,
                           ModelMapper modelMapper,
                           XmlParser xmlParser,
                           PictureService pictureService) {
        this.teamRepository = teamRepository;
        this.validatorUtil = validatorUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.pictureService = pictureService;
    }

    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
        return Files.readString(
                Path.of(TEAMS_FILE_PATH)
        );
    }

    @Override
    public String importTeams() throws JAXBException, FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();

        TeamSeedRootDto teamSeedRootDto = xmlParser
                .fromFile(TEAMS_FILE_PATH, TeamSeedRootDto.class);

        teamSeedRootDto
                .getTeams()
                .forEach(teamSeedDto -> {
                    boolean isValid = validatorUtil.isValid(teamSeedDto);
                    Team exist = teamRepository.findByName(teamSeedDto.getName());

                    if (isValid && exist == null) {
                        Team team = modelMapper.map(teamSeedDto, Team.class);

                        Picture picture = pictureService.getPictureByUrl(
                                teamSeedDto.getPicture().getUrl()
                        );

                        if (picture == null) {
                            stringBuilder.append("Invalid team");
                            stringBuilder.append(System.lineSeparator());
                            return;
                        }

                        //слагаме на Team entity на пикчъра cascade = CascadeType.PERSIST
                        team.setPicture(picture);

                        stringBuilder.append(
                                String.format("Successfully imported team - %s",
                                        teamSeedDto.getName()
                                )
                        );

                        teamRepository.saveAndFlush(team);
                    } else {
                        stringBuilder.append("Invalid team");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }

    @Override
    public Team getTeamByName(String name) {
        return teamRepository.findByName(name);
    }
}
