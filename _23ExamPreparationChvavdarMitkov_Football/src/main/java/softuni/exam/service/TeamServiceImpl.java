package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.fromXml.TeamSeedRoot11Dto;
import softuni.exam.domain.entity.Picture;
import softuni.exam.domain.entity.Team;
import softuni.exam.repository.TeamRepository;
import softuni.exam.util.ValidatorUtilImpl;
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
    private final ValidatorUtilImpl validatorUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    //слагаме на Team entity на пикчъра cascade = CascadeType.PERSIST
    private final PictureService pictureService;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository,
                           ValidatorUtilImpl validatorUtil,
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
    public String importTeams() throws JAXBException, FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();

        TeamSeedRoot11Dto teamSeedRoot11Dto = xmlParser
                .fromFile(TEAMS_FILE_PATH, TeamSeedRoot11Dto.class);

        teamSeedRoot11Dto
                .getTeams()
                .forEach(teamSeed22Dto -> {
                    //ToDo: не валидира правилно
                    boolean isValid = validatorUtil.isValid(teamSeed22Dto);
                    Team byName = teamRepository.findByName(teamSeed22Dto.getName());

                    if (isValid && byName == null) {
                        Team team = modelMapper.map(teamSeed22Dto, Team.class);

                        Picture picture = pictureService.getPictureByUrl(
                                teamSeed22Dto.getPicture().getUrl()
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
                                        teamSeed22Dto.getName()
                                )
                        );

                        teamRepository.save(team);
                    } else {
                        stringBuilder.append("Invalid team");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
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
    public Team getTeamByName(String name) {
        return teamRepository.findByName(name);
    }
}
