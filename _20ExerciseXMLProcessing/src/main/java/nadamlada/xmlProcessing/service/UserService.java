package nadamlada.xmlProcessing.service;

import nadamlada.xmlProcessing.model.dto.UserSeedDto;
import nadamlada.xmlProcessing.model.dto.UserViewRootDto;
import nadamlada.xmlProcessing.model.entity.User;

import java.util.List;

public interface UserService {
    long getCount();

    void seedUsers(List<UserSeedDto> users);

    User getRandomUser();

    UserViewRootDto findUsersWithMoreThanOneSoldProducts();
}
