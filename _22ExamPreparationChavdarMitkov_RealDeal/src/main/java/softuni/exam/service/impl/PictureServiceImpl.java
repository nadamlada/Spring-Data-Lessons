package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.entity.Picture;
import softuni.exam.models.dto.fromJSON.PictureSeedDto;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.PictureService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class PictureServiceImpl implements PictureService {
    public static final String PICTURES_FILE_PATH =
            "src/main/resources/files/json/pictures.json";

    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final CarService carService;

    public PictureServiceImpl(PictureRepository pictureRepository,
                              ModelMapper modelMapper,
                              ValidationUtil validationUtil,
                              Gson gson,
                              CarService carService) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.carService = carService;
    }

    @Override
    public boolean areImported() {
        return pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return Files.readString(
                Path.of(PICTURES_FILE_PATH)
        );
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        PictureSeedDto[] pictureSeedDtos = gson
                .fromJson(readPicturesFromFile(), PictureSeedDto[].class);
        //първи дебъг

        Arrays.stream(pictureSeedDtos)
                .filter(pictureSeedDto -> {
                            boolean isValid = validationUtil.isValid(pictureSeedDto);
                            stringBuilder
                                    .append(isValid
                                                    ? String.format("Successfully import picture - %s",
                                                    pictureSeedDto.getName()
                                            )
                                                    : "Invalid picture"
                                    )
                                    .append(System.lineSeparator());

                            return isValid;
                        }
                )

                .map(pictureSeedDto -> {
                            Picture picture = modelMapper.map(
                                    pictureSeedDto, Picture.class
                            );
                            picture.setCar(
                                    //findById създавам аз, не е по задание
                                    carService.findById(pictureSeedDto.getCarId()
                                    )
                            );

                            return picture;
                        }
                )
                .forEach(pictureRepository::save);

        return stringBuilder.toString();
    }
}
