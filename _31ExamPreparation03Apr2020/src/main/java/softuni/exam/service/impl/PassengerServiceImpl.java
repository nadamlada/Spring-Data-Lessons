package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.fromJson.Passengers.PassengerRootSeedDto;
import softuni.exam.models.entity.Passenger;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static softuni.exam.Paths.MyPaths.PASSENGERS_FILE_PATH;

@Service
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final TownService townService;

    @Autowired
    public PassengerServiceImpl(PassengerRepository passengerRepository,
                                ValidationUtil validationUtil,
                                ModelMapper modelMapper,
                                Gson gson,
                                TownService townService) {
        this.passengerRepository = passengerRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return Files.readString(
                Path.of(PASSENGERS_FILE_PATH)
        );
    }

    @Override
    public String importPassengers() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        PassengerRootSeedDto[] passengerRootSeedDtos = gson
                .fromJson(readPassengersFileContent(), PassengerRootSeedDto[].class);

        Arrays.stream(passengerRootSeedDtos)
                .forEach(passengerRootSeedDto -> {
                    boolean valid = validationUtil.isValid(passengerRootSeedDto);
                    Passenger exist = passengerRepository.getPassengerByAge(
                            passengerRootSeedDto.getAge()
                    );

                    if (valid && exist == null) {
                        Passenger passenger = modelMapper.map(passengerRootSeedDto, Passenger.class);
                        Town town = townService.getTownByName(
                                passengerRootSeedDto.getTown()
                        );

                        passenger.setTown(town);

                        stringBuilder.append(String.format(
                                "Successfully imported Passenger %s - %s",
                                passenger.getLastName(),
                                passenger.getEmail()
                        ));

                        passengerRepository.saveAndFlush(passenger);
                    } else {
                        stringBuilder.append("Invalid Passenger");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }

    @Override
    public Passenger getPassengerByEmail(String email) {
        return passengerRepository.getPassengerByEmail(email);
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder stringBuilder = new StringBuilder();
        passengerRepository
                .bestPassengers()
                .forEach(passenger -> {
                    stringBuilder.append(String.format(
                            """
                                    Passenger %s  %s
                                    	Email - %s
                                        Phone - %s
                                    	Number of tickets - %d
                                    	
                                    """,
                            passenger.getFirstName(),
                            passenger.getLastName(),
                            passenger.getEmail(),
                            passenger.getPhoneNumber(),
                            passenger.getTickets().size()
                    ));
                });

        return stringBuilder.toString().trim();
    }
}
