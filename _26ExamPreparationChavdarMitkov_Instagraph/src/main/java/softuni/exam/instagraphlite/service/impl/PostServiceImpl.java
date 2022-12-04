package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.fromXml.PostSeedRootDto;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.models.entity.Post;
import softuni.exam.instagraphlite.models.entity.User;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.PostService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;
import softuni.exam.instagraphlite.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.instagraphlite.Constants.Paths.POSTS_FILE_PATH;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    //(cascade = CascadeType.MERGE)
    private final UserService userService;
    //(cascade = CascadeType.MERGE)
    private final PictureService pictureService;
    private final XmlParser xmlParser;

    public PostServiceImpl(PostRepository postRepository,
                           ValidationUtil validationUtil,
                           ModelMapper modelMapper,
                           UserService userService,
                           PictureService pictureService,
                           XmlParser xmlParser) {
        this.postRepository = postRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.pictureService = pictureService;
        this.xmlParser = xmlParser;
    }


    @Override
    public boolean areImported() {
        return postRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(
                Path.of(POSTS_FILE_PATH)
        );
    }

    @Override
    public String importPosts() throws IOException, JAXBException {
        StringBuilder stringBuilder = new StringBuilder();

        PostSeedRootDto postSeedRootDto = xmlParser
                .fromFile(POSTS_FILE_PATH, PostSeedRootDto.class);

//        System.out.println();
//        първи дебъг тук

        postSeedRootDto
                .getPosts()
                .forEach(postSeedDto -> {
                    boolean valid = validationUtil.isValid(postSeedDto);
                    User userByUsername = userService
                            .getByUsername(postSeedDto.getUser().getUsername());
                    Picture pictureByPath = pictureService
                            .findByPath(postSeedDto.getPicture().getPath());

                    if (valid && userByUsername != null && pictureByPath != null) {
                        Post post = modelMapper.map(postSeedDto, Post.class);

                        stringBuilder.append(String.format("Successfully imported Post, made by %s",
                                postSeedDto.getUser().getUsername()
                        ));

                        post.setUser(userByUsername);
                        post.setPicture(pictureByPath);

                        postRepository.saveAndFlush(post);
                    } else {
                        stringBuilder.append("Invalid Post");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }
}