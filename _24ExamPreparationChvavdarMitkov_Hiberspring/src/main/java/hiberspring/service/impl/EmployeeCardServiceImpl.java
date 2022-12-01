package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dto.fromJson.EmployeeCardSeedDto;
import hiberspring.domain.entity.EmployeeCard;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.service.EmployeeCardService;
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
public class EmployeeCardServiceImpl implements EmployeeCardService {
    private final EmployeeCardRepository employeeCardRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    @Autowired
    public EmployeeCardServiceImpl(EmployeeCardRepository employeeCardRepository,
                                   ValidationUtil validationUtil,
                                   ModelMapper modelMapper, Gson gson) {
        this.employeeCardRepository = employeeCardRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }


    @Override
    public Boolean employeeCardsAreImported() {
        return employeeCardRepository.count() > 0;
    }

    @Override
    public String readEmployeeCardsJsonFile() throws IOException {
        return Files.readString(
                Path.of(EMPLOYEE_CARDS_FILE_PATH)
        );
    }

    @Override
    public String importEmployeeCards(String employeeCardsFileContent) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        EmployeeCardSeedDto[] employeeCardSeedDtos = gson
                .fromJson(readEmployeeCardsJsonFile(), EmployeeCardSeedDto[].class);

        Arrays.stream(employeeCardSeedDtos)
                .forEach(employeeCardSeedDto -> {
                    boolean isValid = validationUtil.isValid(employeeCardSeedDto);
                    EmployeeCard exists = employeeCardRepository
                            .findByNumber(employeeCardSeedDto.getNumber());

                    if (isValid && exists == null) {
                        EmployeeCard employeeCard =
                                modelMapper.map(employeeCardSeedDto, EmployeeCard.class);

                        stringBuilder.append(String.format(
                                SUCCESSFUL_IMPORT_MESSAGE,
                                "Employee Card",
                                employeeCardSeedDto.getNumber()

                        ));

                        employeeCardRepository.saveAndFlush(employeeCard);
                    } else {
                        stringBuilder.append(INCORRECT_DATA_MESSAGE);
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }

    @Override
    public EmployeeCard getCardByNumber(String number) {
       return employeeCardRepository.findByNumber(number);
    }
}
