package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.fromXml.OfferSeedRootDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.Constants.Paths.OFFERS_FILE_PATH;

@Service
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    //(cascade = CascadeType.MERGE)
    private final AgentService agentService;
    //(cascade = CascadeType.MERGE)
    private final ApartmentService apartmentService;

    public OfferServiceImpl(OfferRepository offerRepository,
                            ValidationUtil validationUtil,
                            ModelMapper modelMapper,
                            XmlParser xmlParser,
                            AgentService agentService, ApartmentService apartmentService) {
        this.offerRepository = offerRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.agentService = agentService;
        this.apartmentService = apartmentService;
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
        OfferSeedRootDto offerSeedRootDto = xmlParser
                .fromFile(OFFERS_FILE_PATH, OfferSeedRootDto.class);

        offerSeedRootDto
                .getOffers()
                .forEach(offerSeedDto -> {
                            boolean valid = validationUtil.isValid(offerSeedDto);
                            Agent agentExists = agentService.getAgentByFirstName(
                                    offerSeedDto.getAgent().getFirstName()
                            );

                            Apartment apartmentExists = apartmentService.getApartmentById(
                                    offerSeedDto.getApartment().getId()
                            );

//                            System.out.println();
                            if (valid && agentExists != null) {
                                Offer offer = modelMapper.map(offerSeedDto, Offer.class);
                                offer.setAgent(agentExists);
                                offer.setApartment(apartmentExists);

                                stringBuilder.append(String.format(
                                        "Successfully imported offer %.2f",
                                        offerSeedDto.getPrice()
                                ));

                                offerRepository.saveAndFlush(offer);
                            } else {
                                stringBuilder.append("Invalid offer");
                            }

                            stringBuilder.append(System.lineSeparator());
                        }
                );

        return stringBuilder.toString().trim();
    }

    @Override
    public String exportOffers() {
//        StringBuilder stringBuilder = new StringBuilder();
//        List<Offer> offers = offerRepository
//                .lastQuery();
//
//        offers.forEach(offer -> {
//
//            stringBuilder.append(String.format(
//            """
//                    Agent %s %s with offer №%s:
//                    -Apartment area: %.2f
//                    --Town: %s
//                    ---Price: %.2f$
//                             """,
//                    offer.getAgent().getFirstName(),
//                    offer.getAgent().getLastName(),
//                    offer.getId(),
//                    offer.getApartment().getArea(),
//                    offer.getApartment().getTown(),
//                    offer.getPrice()
//            ));
//        });
//
//        return stringBuilder.toString().trim();
//    }
        return null;
    }
}
