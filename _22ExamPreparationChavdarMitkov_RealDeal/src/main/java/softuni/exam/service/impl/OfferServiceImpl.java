package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.entity.Offer;
import softuni.exam.models.dto.fromXML.OfferSeedRoot1Dto;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.OfferService;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class OfferServiceImpl implements OfferService {
    public static final String OFFERS_FILE_PATH =
            "src/main/resources/files/xml/offers.xml";

    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final CarService carService;
    private final SellerService sellerService;

    public OfferServiceImpl(OfferRepository offerRepository,
                            ModelMapper modelMapper,
                            ValidationUtil validationUtil,
                            XmlParser xmlParser,
                            CarService carService,
                            SellerService sellerService) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.carService = carService;
        this.sellerService = sellerService;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(
                Path.of(OFFERS_FILE_PATH)
        );
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder stringBuilder = new StringBuilder();

        //първи дебъг е само с това
//        OfferSeedRoot1Dto offerSeedRoot1Dto = xmlParser
//                .fromFile(OFFERS_FILE_PATH, OfferSeedRoot1Dto.class);

        OfferSeedRoot1Dto offerSeedRoot1Dto = xmlParser
                .fromFile(OFFERS_FILE_PATH, OfferSeedRoot1Dto.class);

        offerSeedRoot1Dto
                .getOffers()
                .stream().filter(offerSeed2Dto -> {
                            boolean isValid = validationUtil.isValid(offerSeed2Dto);
                            stringBuilder.append(
                                            isValid ? String.format("Successfully import offer %s - %s",
                                                    offerSeed2Dto.getAddedOn(),
                                                    offerSeed2Dto.isHasGoldStatus()
                                            )
                                                    : "Invalid offer"
                                    )
                                    .append(System.lineSeparator());

                            return isValid;
                        }
                )
                .map(offerSeed2Dto -> {
                            Offer offer = modelMapper.map(offerSeed2Dto, Offer.class);
                            offer.setSeller(sellerService
                                    .findById(offerSeed2Dto.getSeller().getId())
                            );
                            offer.setCar(carService.
                                    findById(offerSeed2Dto.getCar().getId())
                            );

                            return offer;
                        }
                )
                .forEach(offerRepository::save);

        return stringBuilder.toString();
    }
}
