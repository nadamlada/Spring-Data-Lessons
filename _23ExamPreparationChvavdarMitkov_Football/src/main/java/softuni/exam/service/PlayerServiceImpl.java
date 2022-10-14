package softuni.exam.service;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.fromJson.PlayerSeedDto;
import softuni.exam.domain.entity.Picture;
import softuni.exam.domain.entity.Player;
import softuni.exam.domain.entity.Team;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.util.ValidatorUtilImpl;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {
    public static final String PLAYERS_FILE_PATH =
            "src/main/resources/files/json/players.json";

    private final PlayerRepository playerRepository;
    private final ValidatorUtilImpl validatorUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    //щом викаме тези чужди сървиси в плеъра на полетата team и picture (cascade = CascadeType.ALL)
    private final TeamService teamService;
    private final PictureService pictureService;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository,
                             ValidatorUtilImpl validatorUtil,
                             ModelMapper modelMapper,
                             Gson gson,
                             TeamService teamService,
                             PictureService pictureService) {
        this.playerRepository = playerRepository;
        this.validatorUtil = validatorUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.teamService = teamService;
        this.pictureService = pictureService;
    }

    @Override

    public String importPlayers() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        PlayerSeedDto[] playerSeedDtos = gson
                .fromJson(readPlayersJsonFile(), PlayerSeedDto[].class);

        Arrays.stream(playerSeedDtos)
                .forEach(playerSeedDto -> {
                    boolean isValid = validatorUtil.isValid(playerSeedDtos);
                    Player byFirstNameAndLastName = playerRepository.findByFirstNameAndLastName(
                            playerSeedDto.getFirstName(),
                            playerSeedDto.getLastName());

                    if (isValid && byFirstNameAndLastName == null) {
                        Player player = modelMapper.map(playerSeedDto, Player.class);
                        Team team = teamService.getTeamByName(playerSeedDto.getTeam().getName());
                        Picture picture = pictureService.getPictureByUrl(playerSeedDto.getPicture().getUrl());

                        player.setPicture(picture);
                        player.setTeam(team);

                        stringBuilder.append(
                                String.format("Successfully imported player - %s %s",
                                        playerSeedDto.getFirstName(),
                                        playerSeedDto.getLastName()
                                )
                        );

                        this.playerRepository.save(player);
                    } else {
                        stringBuilder.append("Invalid player");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }

    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return Files.readString(
                Path.of(PLAYERS_FILE_PATH)
        );
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        StringBuilder stringBuilder = new StringBuilder();
        List<Player> players = playerRepository
                .findAllBySalaryGreaterThanOrderBySalaryDesc(BigDecimal.valueOf(1000000));

        players
                .forEach(player -> {
                    stringBuilder.append(
                            String.format("Player name: %s %s%n" +
                                          "\tNumber: %d%n" +
                                          "\tSalary: %.2f%n" +
                                          "\tTeam: %s%n",
                                    player.getFirstName(),
                                    player.getLastName(),
                                    player.getNumber(),
                                    player.getSalary(),
                                    player.getTeam().getName()
                            )
                    );
                });

        return stringBuilder.toString().trim();
    }

    @Override
    public String exportPlayersInATeam() {
        StringBuilder stringBuilder = new StringBuilder();
        List<Player> players = playerRepository.findAllByTeamName("North Hub");

        players
                .forEach(player -> {
                    stringBuilder.append(
                            String.format("Player name: %s %s - %s%n" +
                                          "Number: %d%n",
                                    player.getFirstName(),
                                    player.getLastName(),
                                    player.getPosition(),
                                    player.getNumber()
                            )
                    );
                });

        return stringBuilder.toString().trim();
    }
}
