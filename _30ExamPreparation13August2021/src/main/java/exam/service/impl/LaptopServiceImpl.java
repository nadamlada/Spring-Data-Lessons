package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.fromJson.laptops.LaptopSeedRootDto;
import exam.model.entity.Laptop;
import exam.model.entity.Shop;
import exam.repository.LaptopRepository;
import exam.service.LaptopService;
import exam.service.ShopService;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static exam.paths.MyPaths.LAPTOPS_FILES_PATH;

@Service
public class LaptopServiceImpl implements LaptopService {
    private final LaptopRepository laptopRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ShopService shopService;

    @Autowired
    public LaptopServiceImpl(LaptopRepository laptopRepository,
                             ValidationUtil validationUtil,
                             ModelMapper modelMapper,
                             Gson gson,
                             XmlParser xmlParser,
                             ShopService shopService) {
        this.laptopRepository = laptopRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.shopService = shopService;
    }

    @Override
    public boolean areImported() {
        return laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return Files.readString(
                Path.of(LAPTOPS_FILES_PATH)
        );
    }

    @Override
    public String importLaptops() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        LaptopSeedRootDto[] laptopSeedRootDtos = gson.
                fromJson(readLaptopsFileContent(), LaptopSeedRootDto[].class);

        Arrays.stream(laptopSeedRootDtos)
                .forEach(laptopSeedRootDto -> {
                    boolean valid = validationUtil.isValid(laptopSeedRootDto);
                    Laptop exist = laptopRepository.getLaptopByMacAddress(
                            laptopSeedRootDto.getMacAddress()
                    );

                    if (valid && exist == null) {
                        Laptop laptop = modelMapper.map(laptopSeedRootDto, Laptop.class);
                        Shop shop = shopService.getShopByName(
                                laptopSeedRootDto.getShop().getName()
                        );

                        laptop.setShop(shop);

                        stringBuilder.append(String.format(
                                "Successfully imported Laptop %s - %.2f - %d - %d",
                                laptopSeedRootDto.getMacAddress(),
                                laptopSeedRootDto.getCpuSpeed(),
                                laptopSeedRootDto.getRam(),
                                laptopSeedRootDto.getStorage()
                        ));

                        laptopRepository.saveAndFlush(laptop);
                    } else {
                        stringBuilder.append("Invalid Laptop");
                    }
                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString();
    }

    @Override
    public String exportBestLaptops() {
        StringBuilder stringBuilder = new StringBuilder();
        List<Laptop> all = laptopRepository
                .getAllByIdNotNullOrderByCpuSpeedDescRamDescStorageDescMacAddress();

        all.forEach(laptop -> {
            stringBuilder.append(String.format(
                    """ 
                        Laptop - %s
                        *Cpu speed - %.2f
                        **Ram - %d
                        ***Storage - %d
                        ****Price - %.2f
                        #Shop name - %s
                        ##Town - %s
                        
                    """,
                    laptop.getMacAddress(),
                    laptop.getCpuSpeed(),
                    laptop.getRam(),
                    laptop.getStorage(),
                    laptop.getPrice(),
                    laptop.getShop().getName(),
                    laptop.getShop().getTown().getName()
            ));
        });

        return stringBuilder.toString().trim();
    }
}