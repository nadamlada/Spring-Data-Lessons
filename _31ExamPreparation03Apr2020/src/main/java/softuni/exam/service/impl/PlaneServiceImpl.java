package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.fromXml.planes.PlaneSeedRootDto;
import softuni.exam.models.entity.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.Paths.MyPaths.PLANES_FILE_PATH;

@Service
public class PlaneServiceImpl implements PlaneService {
    private final PlaneRepository planeRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    @Autowired
    public PlaneServiceImpl(PlaneRepository planeRepository,
                            ValidationUtil validationUtil,
                            ModelMapper modelMapper,
                            XmlParser xmlParser) {
        this.planeRepository = planeRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return Files.readString(
                Path.of(PLANES_FILE_PATH)
        );
    }

    @Override
    public String importPlanes() throws JAXBException, FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();
        PlaneSeedRootDto planeSeedRootDto = xmlParser.
                fromFile(PLANES_FILE_PATH, PlaneSeedRootDto.class);

        planeSeedRootDto
                .getPlanes()
                .forEach(planeSeedDto -> {
                    boolean valid = validationUtil.isValid(planeSeedDto);

                    Plane exist = planeRepository.getPlaneByRegisterNumber(
                            planeSeedDto.getRegisterNumber());

                    if(valid && exist == null) {
                        Plane plane = modelMapper.map(planeSeedDto, Plane.class);

                        stringBuilder.append(String.format(
                                "Successfully imported Plane %s",
                                planeSeedDto.getRegisterNumber()
                        ));

                        planeRepository.saveAndFlush(plane);
                    } else {
                        stringBuilder.append("Invalid Plane");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }

    @Override
    public Plane getPlaneByRegisterNumber(String registerNumber) {
        return planeRepository.getPlaneByRegisterNumber(registerNumber);
    }
}
