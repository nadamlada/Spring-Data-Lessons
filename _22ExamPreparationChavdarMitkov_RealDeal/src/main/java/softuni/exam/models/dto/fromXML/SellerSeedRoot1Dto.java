package softuni.exam.models.dto.fromXML;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sellers")
@XmlAccessorType(XmlAccessType.FIELD)
//валидациите на полетата или на гетъра
public class SellerSeedRoot1Dto {
    @XmlElement(name = "seller")
    private List<SellerSeed2Dto> sellers;

    public SellerSeedRoot1Dto() {
    }

    public List<SellerSeed2Dto> getSellers() {
        return sellers;
    }

    public void setSellers(List<SellerSeed2Dto> sellers) {
        this.sellers = sellers;
    }
}
