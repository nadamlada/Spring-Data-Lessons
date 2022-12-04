package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.fromJson.PictureSeedDto;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

import static softuni.exam.instagraphlite.Constants.Paths.PICTURES_FILE_PATH;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public PictureServiceImpl(PictureRepository pictureRepository,
                              ValidationUtil validationUtil,
                              ModelMapper modelMapper,
                              Gson gson) {
        this.pictureRepository = pictureRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return pictureRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(
                Path.of(PICTURES_FILE_PATH)
        );
    }

    @Override
    public String importPictures() throws IOException {
        PictureSeedDto[] pictureSeedDtos = gson.
                fromJson(readFromFileContent(), PictureSeedDto[].class);

//        System.out.println();
//        Debug here

        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(pictureSeedDtos)
                .forEach(pictureSeedDto -> {
                    boolean valid = validationUtil.isValid(pictureSeedDto);
                    Picture exists = pictureRepository
                            .findByPath(pictureSeedDto
                                    .getPath());

                    if (valid && exists == null) {
                        Picture picture = modelMapper.map(pictureSeedDto, Picture.class);

                        stringBuilder.append(String.format(
                                "Successfully imported Picture, with size %.2f",
                                pictureSeedDto.getSize()
                        ));

                        pictureRepository.saveAndFlush(picture);
                    } else {
                        stringBuilder.append("Invalid picture");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }

    @Override
    public String exportPictures() {
        StringBuilder stringBuilder = new StringBuilder();
        pictureRepository
                .findAllBySizeAfterOrderBySize(30000)
                .forEach(picture -> {
                    stringBuilder.append(String.format(
                    "%.2f - %s%n",
                            picture.getSize(),
                            picture.getPath()
                            ));
                });

        return stringBuilder.toString().trim();
    }

    @Override
    public Picture findByPath(String path) {
        return pictureRepository.findByPath(path);
    }
}
