package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.fromXml.pictures.PictureSeedRootDto;
import softuni.exam.domain.entity.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.PictureService;
import softuni.exam.util.impl.ValidationUtilImpl;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PictureServiceImpl implements PictureService {
    public static final String PICTURES_FILE_PATH =
            "src/main/resources/files/xml/pictures.xml";

    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtilImpl validatorUtil;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository,
                              ModelMapper modelMapper,
                              XmlParser xmlParser,
                              ValidationUtilImpl validatorUtil) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public boolean areImported() {
        return pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {
        return Files.readString(
                Path.of(PICTURES_FILE_PATH)
        );
    }

    @Override
    public String importPictures() throws IOException, JAXBException {
        StringBuilder stringBuilder = new StringBuilder();

        PictureSeedRootDto pictureSeedRootDto =
                xmlParser.fromFile(PICTURES_FILE_PATH, PictureSeedRootDto.class);

        pictureSeedRootDto
                .getPictures()
                .forEach(pictureSeedDto -> {
                    boolean isValid = validatorUtil.isValid(pictureSeedDto);
                    Picture exists = pictureRepository.findByUrl(pictureSeedDto.getUrl());

                    if (isValid && exists == null) {
                        Picture picture = modelMapper.map(pictureSeedDto, Picture.class);

                        stringBuilder.append(
                                String.format("Successfully imported picture - %s",
                                        pictureSeedDto.getUrl()
                                )
                        );

                        pictureRepository.saveAndFlush(picture);
                    } else {
                        stringBuilder.append("Invalid picture");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }

    @Override
    public Picture getPictureByUrl(String url) {
        return pictureRepository.findByUrl(url);
    }
}