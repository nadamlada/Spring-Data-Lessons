package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.fromXml.tickets.TicketSeedRootDto;
import softuni.exam.models.entity.Passenger;
import softuni.exam.models.entity.Plane;
import softuni.exam.models.entity.Ticket;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TicketRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.PlaneService;
import softuni.exam.service.TicketService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.Paths.MyPaths.TICKETS_FILE_PATH;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final PassengerService passengerService;
    private final PlaneService planeService;
    private final TownService townService;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository,
                             ValidationUtil validationUtil,
                             ModelMapper modelMapper,
                             XmlParser xmlParser,
                             PassengerService passengerService,
                             PlaneService planeService,
                             TownService townService) {
        this.ticketRepository = ticketRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.passengerService = passengerService;
        this.planeService = planeService;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files.readString(
                Path.of(TICKETS_FILE_PATH)
        );
    }

    @Override
    public String importTickets() throws JAXBException, FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();
        TicketSeedRootDto ticketSeedRootDto = xmlParser
                .fromFile(TICKETS_FILE_PATH, TicketSeedRootDto.class);

        ticketSeedRootDto
                .getTickets()
                .forEach(ticketSeedDto -> {
                    boolean valid = validationUtil.isValid(ticketSeedDto);
                    Ticket exist = ticketRepository.getTicketBySerialNumber(
                            ticketSeedDto.getSerialNumber()
                    );

                    if (valid && exist == null) {
                        Ticket ticket = modelMapper.map(ticketSeedDto, Ticket.class);
                        Passenger passenger = passengerService.getPassengerByEmail(
                                ticketSeedDto.getPassenger().getEmail()
                        );

                        Plane plane = planeService.getPlaneByRegisterNumber(
                                ticketSeedDto.getPlane().getRegisterNumber()
                        );

                        Town townTo = townService.getTownByName(
                                ticketSeedDto.getToTown().getName()
                        );

                        Town townFrom = townService.getTownByName(
                                ticketSeedDto.getFromTown().getName()
                        );

                        ticket.setPassenger(passenger);
                        ticket.setPlane(plane);
                        ticket.setToTown(townTo);
                        ticket.setFromTown(townFrom);

                        stringBuilder.append(String.format(
                                "Successfully imported Ticket %s - %s",
                                ticketSeedDto.getFromTown().getName(),
                                ticketSeedDto.getToTown().getName()
                        ));

                        ticketRepository.saveAndFlush(ticket);
                    } else {
                        stringBuilder.append("Invalid Ticket");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }
}
