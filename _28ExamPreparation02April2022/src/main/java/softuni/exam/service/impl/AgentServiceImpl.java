package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.fromJson.AgentSeedDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.repository.AgentRepository;
import softuni.exam.service.AgentService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static softuni.Constants.Paths.AGENTS_FILE_PATH;

@Service
public class AgentServiceImpl implements AgentService {
    private final AgentRepository agentRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public AgentServiceImpl(AgentRepository agentRepository,
                            ValidationUtil validationUtil,
                            ModelMapper modelMapper,
                            Gson gson) {
        this.agentRepository = agentRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() throws IOException {
        return agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files.readString(
                Path.of(AGENTS_FILE_PATH)
        );
    }

    @Override
    public String importAgents() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        AgentSeedDto[] agentSeedDtos = gson
                .fromJson(readAgentsFromFile(), AgentSeedDto[].class);

        Arrays.stream(agentSeedDtos)
                .forEach(agentSeedDto -> {
                    boolean valid = validationUtil.isValid(agentSeedDto);

                    Agent exist = agentRepository.findByFirstNameOrEmail(
                            agentSeedDto.getFirstName(),
                            agentSeedDto.getEmail()
                    );

                    if (valid && exist == null) {
                        Agent agent = modelMapper.map(agentSeedDto, Agent.class);

                        stringBuilder.append(String.format(
                                "Successfully imported agent - %s %s",
                                agentSeedDto.getFirstName(),
                                agentSeedDto.getLastName()
                        ));

                        agentRepository.saveAndFlush(agent);

                    } else {
                        stringBuilder.append("Invalid agent");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }

    @Override
    public Agent getAgentByFirstName(String firstName) {
        return agentRepository.findByFirstName(firstName);
    }
}
