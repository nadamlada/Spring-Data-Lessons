package com.example.football.service.impl;

import com.example.football.models.dto.fromXml.PlayersSeedRootDto;
import com.example.football.models.entity.Player;
import com.example.football.models.entity.Stat;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.PlayerRepository;
import com.example.football.service.PlayerService;
import com.example.football.service.StatService;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
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
import java.time.LocalDate;

import static com.example.football.paths.Paths.PLAYERS_FILE_PATH;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    //(cascade = CascadeType.MERGE)
    private final TeamService teamService;
    //(cascade = CascadeType.MERGE)
    private final StatService statService;
    //(cascade = CascadeType.MERGE)
    private final TownService townService;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository,
                             ValidationUtil validationUtil,
                             ModelMapper modelMapper,
                             XmlParser xmlParser,
                             TeamService teamService,
                             StatService statService,
                             TownService townService) {
        this.playerRepository = playerRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.teamService = teamService;
        this.statService = statService;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(
                Path.of(PLAYERS_FILE_PATH)
        );
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();
        PlayersSeedRootDto playersSeedRootDto = xmlParser
                .fromFile(PLAYERS_FILE_PATH, PlayersSeedRootDto.class);

        playersSeedRootDto
                .getPlayers()
                .forEach(playerSeedDto -> {
                    boolean valid = validationUtil.isValid(playerSeedDto);
                    Player exist = playerRepository.getPlayerByEmail(
                            playerSeedDto.getEmail()
                    );

                    if (valid && exist == null) {
                        Player player = modelMapper.map(playerSeedDto, Player.class);
                        Team team = teamService.getTeamsByName(
                                playerSeedDto.getTeamNameSeedDto().getName()
                        );
                        Stat stat = statService.getStatById(
                                playerSeedDto.getStatIdSeedDto().getId()
                        );

                        Town town = townService.getTownByTownName(
                                playerSeedDto.getTownNameSeedDto().getName()
                        );

                        stringBuilder.append(String.format(
                                "Successfully imported Player %s %s - %s",
                                playerSeedDto.getFirstName(),
                                playerSeedDto.getLastName(),
                                playerSeedDto.getPosition().toString()
                        ));

                        player.setTeam(team);
                        player.setStat(stat);
                        player.setTown(town);

                        playerRepository.saveAndFlush(player);
                    } else {
                        stringBuilder.append("Invalid Player");
                    }

                    stringBuilder.append(System.lineSeparator());
                });


        return stringBuilder.toString().trim();
    }

    @Override
    public String exportBestPlayers() {
        LocalDate after = LocalDate.of(1995, 01, 01);
        LocalDate before = LocalDate.of(2003, 01, 01);

        StringBuilder stringBuilder = new StringBuilder();
        playerRepository
                .lastMethod(after,before)
                .forEach(player -> {
                    stringBuilder.append(String.format(
                            """
                            Player - %s %s
                                    Position - %s
                                    Team - %s
                                    Stadium - %s
                                    """,
                            player.getFirstName(),
                            player.getLastName(),
                            player.getPosition().toString(),
                            player.getTeam().getName(),
                            player.getTeam().getStadiumName()
                    ));
                });

        return stringBuilder.toString().trim();
    }
}
