package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.fromJson.UserSeedDto;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.models.entity.User;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static softuni.exam.instagraphlite.Constants.Paths.USERS_FILE_PATH;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final PictureService pictureService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           ValidationUtil validationUtil,
                           ModelMapper modelMapper,
                           Gson gson,
                           PictureService pictureService) {
        this.userRepository = userRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.pictureService = pictureService;
    }

    @Override
    public boolean areImported() {
        return userRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(
                Path.of(USERS_FILE_PATH)
        );
    }

    @Override
    public String importUsers() throws IOException {
        //2:13 от видеото
        StringBuilder stringBuilder = new StringBuilder();
        UserSeedDto[] userSeedDtos = gson
                .fromJson(readFromFileContent(), UserSeedDto[].class);

        System.out.println();
//        първи дебъг

        Arrays.stream(userSeedDtos)
                .forEach(userSeedDto -> {
                    boolean valid = validationUtil.isValid(userSeedDto);
                    User exists = userRepository.getByUsername(userSeedDto.getUsername());
                    Picture pictureByPath = pictureService.findByPath(userSeedDto.getProfilePicture());

                    if (valid && pictureByPath != null && exists == null) {
                        User user = modelMapper.map(userSeedDto, User.class);

                        user.setProfilePicture(pictureByPath);

                        stringBuilder.append(String.format(
                                "Successfully imported User: %s",
                                userSeedDto.getUsername()
                        ));

                        userRepository.saveAndFlush(user);
                    } else {
                        stringBuilder.append("Invalid User");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }

    @Override
    public String exportUsersWithTheirPosts() {
        StringBuilder stringBuilder = new StringBuilder();

        userRepository
                .findAllByPostsOrderByCountOfPostsDescAndUserId()
                .forEach(user -> {
                    stringBuilder.append(String.format(
                            """
                                    User: %s
                                    Post count: %d
                                    """,
                            user.getUsername(),
                            user.getPosts().size()
                    ));

                    user.getPosts()
                            .forEach(post -> {
                                stringBuilder.append(String.format(
                                        """
                                                ==Post Details:
                                                ----Caption: %s
                                                ----Picture Size: %.2f
                                                """,
                                        post.getCaption(),
                                        post.getPicture().getSize()
                                ));
                            });
                });

        return stringBuilder.toString();
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }
}