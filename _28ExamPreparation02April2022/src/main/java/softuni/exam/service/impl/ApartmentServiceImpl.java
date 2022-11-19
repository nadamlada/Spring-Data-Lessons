package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.fromXml.ApartmentSeedRootDto;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.Constants.Paths.APARTMENTS_FILE_PATH;

@Service
public class ApartmentServiceImpl implements ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    //(cascade = CascadeType.MERGE)
    private final TownService townService;

    public ApartmentServiceImpl(ApartmentRepository apartmentRepository,
                                ValidationUtil validationUtil,
                                ModelMapper modelMapper,
                                XmlParser xmlParser,
                                TownService townService) {
        this.apartmentRepository = apartmentRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.townService = townService;
    }

    @Override
    public boolean areImported() throws IOException {
        return apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(
                Path.of(APARTMENTS_FILE_PATH)
        );
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        StringBuilder stringBuilder = new StringBuilder();
        ApartmentSeedRootDto apartmentSeedRootDto = xmlParser
                .fromFile(APARTMENTS_FILE_PATH, ApartmentSeedRootDto.class);

        apartmentSeedRootDto
                .getApartments()
                .forEach(apartmentSeedDto -> {
                    boolean valid = validationUtil.isValid(apartmentSeedDto);

                    Apartment exists = apartmentRepository
                            .getApartmentByAreaAndTown_TownName(
                                    apartmentSeedDto.getArea(),
                                    apartmentSeedDto.getTown()
                            );

                    if (valid && exists == null) {

                        Apartment apartment = modelMapper.map(apartmentSeedDto, Apartment.class);

                        Town town = townService.getByName(
                                apartmentSeedDto.getTown()
                        );

                        apartment.setTown(town);

                        stringBuilder.append(String.format(
                                "Successfully imported apartment %s - %.2f",
                                apartmentSeedDto.getApartmentType().toString(),
                                apartmentSeedDto.getArea()
                        ));

                        apartmentRepository.saveAndFlush(apartment);
                    } else {
                        stringBuilder.append("Invalid apartment");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString();
    }

    @Override
    public Apartment getApartmentById(Long id) {
       return apartmentRepository.getApartmentById(id);
   }
}
