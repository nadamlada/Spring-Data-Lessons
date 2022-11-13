package nadamlada.xmlprocessing2;

import nadamlada.xmlprocessing2.Repository.UserRepository;
import nadamlada.xmlprocessing2.model.dto._1UserCollectionDto;
import nadamlada.xmlprocessing2.model.dto._2UserWithSoldProductDto;
import nadamlada.xmlprocessing2.model.dto._3SoldProductCollectionDto;
import nadamlada.xmlprocessing2.model.dto._4SoldProductDto;
import nadamlada.xmlprocessing2.model.entity.User;
import nadamlada.xmlprocessing2.services.util.FormatConverter;
import nadamlada.xmlprocessing2.services.util.FormatConverterFactory;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final FormatConverterFactory factory;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public CommandLineRunnerImpl(FormatConverterFactory factory,
                                 ModelMapper modelMapper,
                                 UserRepository userRepository) {
        this.factory = factory;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        Set<User> allWithSoldProducts = userRepository
                .findUserBySoldProductsNotNull();

        _1UserCollectionDto root = new _1UserCollectionDto();
        root.setUsers(allWithSoldProducts
                .stream()
                .map(entity -> {
                            _2UserWithSoldProductDto user = modelMapper.map(entity, _2UserWithSoldProductDto.class);
                            _3SoldProductCollectionDto soldProductCollection = new _3SoldProductCollectionDto();
                            soldProductCollection.setProducts(
                                    entity.getSoldProducts()
                                            .stream().map(product ->
                                                    modelMapper.map(product, _4SoldProductDto.class))
                                            .collect(Collectors.toList())
                            );
                            user.setSoldProductInfo(soldProductCollection);
                            return user;
                        }
                )
                .collect(Collectors.toList()
                )
        );

        FormatConverter json = this.factory.create("json");
        json.setPrettyPrint();
        System.out.println(json.serialize(root));
    }
}
