package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.entity.Seller;
import softuni.exam.models.dto.fromXML.SellerSeedRoot1Dto;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class SellerServiceImpl implements SellerService {
    public static final String SELLERS_FILE_PATH =
            "src/main/resources/files/xml/sellers.xml";

    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public SellerServiceImpl(SellerRepository sellerRepository,
                             ModelMapper modelMapper,
                             ValidationUtil validationUtil,
                             XmlParser xmlParser) {
        this.sellerRepository = sellerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files
                .readString(Path.of(SELLERS_FILE_PATH));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder stringBuilder = new StringBuilder();

        SellerSeedRoot1Dto sellerSeedRoot1Dto = xmlParser
                .fromFile(SELLERS_FILE_PATH, SellerSeedRoot1Dto.class);

        sellerSeedRoot1Dto
                .getSellers()
                .stream()
                .filter(sellerSeed2Dto -> {
                            boolean isValid = validationUtil.isValid(sellerSeed2Dto);
                            stringBuilder.append(
                                            isValid ? String.format("Successfully import seller %s - %s",
                                                    sellerSeed2Dto.getLastName(),
                                                    sellerSeed2Dto.getEmail()
                                            )
                                                    : "Invalid seller"
                                    )
                                    .append(System.lineSeparator());
                            return isValid;
                        }
                )
                .map(sellerSeed2Dto ->
                        modelMapper.map(sellerSeed2Dto, Seller.class)
                )
                .forEach(sellerRepository::save);

        return stringBuilder.toString();
    }

    @Override
    public Seller findById(Long id) {
        return sellerRepository
                .findById(id)
                .orElse(null);
    }
}