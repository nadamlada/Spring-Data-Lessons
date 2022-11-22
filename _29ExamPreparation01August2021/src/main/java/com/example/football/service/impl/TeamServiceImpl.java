package com.example.football.service.impl;

import com.example.football.models.dto.FromJson.TeamSeedDto;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.TeamRepository;
import com.example.football.service.TeamService;
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

import static com.example.football.paths.Paths.TEAMS_FILE_PATH;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    //(cascade = CascadeType.MERGE)
    private final TownService townService;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository,
                           ValidationUtil validationUtil,
                           ModelMapper modelMapper,
                           Gson gson,
                           TownService townService) {
        this.teamRepository = teamRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.townService = townService;
    }


    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return Files.readString(
                Path.of(TEAMS_FILE_PATH)
        );
    }

    @Override
    public String importTeams() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        TeamSeedDto[] teamSeedDtos = gson
                .fromJson(readTeamsFileContent(), TeamSeedDto[].class);

        Arrays.stream(teamSeedDtos)
                .forEach(teamSeedDto -> {
                    boolean valid = validationUtil.isValid(teamSeedDto);
                    Team exist = teamRepository.getTeamsByName(
                            teamSeedDto.getName()
                    );

                    if(valid && exist == null) {
                        Team team = modelMapper.map(teamSeedDto, Team.class);
                        Town town = townService.getTownByTownName(
                                teamSeedDto.getTownName()
                        );

                        stringBuilder.append(String.format(
                                "Successfully imported Team %s- %d",
                                teamSeedDto.getTownName(),
                                teamSeedDto.getFanBase()
                        ));

                        team.setTown(town);
                        teamRepository.saveAndFlush(team);
                    } else {
                        stringBuilder.append("Invalid Team");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }

    @Override
    public Team getTeamsByName(String name) {
       return teamRepository.getTeamsByName(name);
    }
}
