package nadamlada.xmlProcessing.service.impl;

import nadamlada.xmlProcessing.model.dto.UserSeedDto;
import nadamlada.xmlProcessing.model.dto.UserViewRootDto;
import nadamlada.xmlProcessing.model.dto.UserWithOneSoldProductVewDto;
import nadamlada.xmlProcessing.model.entity.User;
import nadamlada.xmlProcessing.repository.UserRepository;
import nadamlada.xmlProcessing.service.UserService;
import nadamlada.xmlProcessing.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public long getCount() {
        return userRepository.count();
    }

    @Override
    public void seedUsers(List<UserSeedDto> users) {
        users
                .stream()
                .filter(validationUtil::isValid)
                .map(userSeedDto ->
                        modelMapper.map(userSeedDto, User.class)
                )
                .forEach(userRepository::save);
    }

    @Override
    public User getRandomUser() {
        long randomId = ThreadLocalRandom
                .current().nextLong(1, userRepository.count() + 1);

        return userRepository
                .findById(randomId)
                .orElse(null);
    }

    @Override
    public UserViewRootDto findUsersWithMoreThanOneSoldProducts() {
        UserViewRootDto userViewRootDto = new UserViewRootDto();

        userViewRootDto.setUserWithOneSoldProductVewDtos(
                userRepository.
                        findAllUsersWithSoldProductsGreaterThanOne()
                        .stream()
                        .map(user ->
                                modelMapper.map(user, UserWithOneSoldProductVewDto.class))
                        .collect(Collectors.toList())
        );

        return userViewRootDto;
    }
}
