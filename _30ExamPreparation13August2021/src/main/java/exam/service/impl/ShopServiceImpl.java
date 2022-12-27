package exam.service.impl;

import exam.model.dto.fromXml.shops.ShopsSeedRootDto;
import exam.model.entity.Shop;
import exam.model.entity.Town;
import exam.repository.ShopRepository;
import exam.service.ShopService;
import exam.service.TownService;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static exam.paths.MyPaths.SHOPS_FILES_PATH;

@Service
public class ShopServiceImpl implements ShopService {
    private final ShopRepository shopRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final TownService townService;

    @Autowired
    public ShopServiceImpl(ShopRepository shopRepository,
                           ValidationUtil validationUtil,
                           ModelMapper modelMapper,
                           XmlParser xmlParser,
                           TownService townService) {
        this.shopRepository = shopRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return Files.readString(
                Path.of(SHOPS_FILES_PATH)
        );
    }

    @Override
    public String importShops() throws JAXBException, FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();
        ShopsSeedRootDto shopsSeedRootDto = xmlParser
                .fromFile(SHOPS_FILES_PATH, ShopsSeedRootDto.class);

        shopsSeedRootDto
                .getShops()
                .forEach(shopSeedDto -> {
                    boolean valid = validationUtil.isValid(shopSeedDto);
                    Shop exists = shopRepository.getShopByName(
                            shopSeedDto.getName()
                    );

                    if (valid && exists == null) {
                        Shop shop = modelMapper.map(shopSeedDto, Shop.class);
                        Town town = townService.getTownByName(
                                shopSeedDto.getTown().getName()
                        );

                        shop.setTown(town);

                        stringBuilder.append(String.format(
                                "Successfully imported Shop %s - %.0f",
                                shopSeedDto.getName(),
                                shopSeedDto.getIncome()
                        ));

                        shopRepository.saveAndFlush(shop);
                    } else {
                        stringBuilder.append("Invalid shop");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }

    @Override
    public Shop getShopByName(String name) {
        return shopRepository.getShopByName(name);
    }
}
