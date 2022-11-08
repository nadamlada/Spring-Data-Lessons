package softuni.exam.models.dto.fromXML;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "offers")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferSeedRoot1Dto {
    @XmlElement(name = "offer")
    private List<OfferSeed2Dto> offers;

    public OfferSeedRoot1Dto() {
    }

    public List<OfferSeed2Dto> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferSeed2Dto> offers) {
        this.offers = offers;
    }
}
