package softuni.exam.models.dto.fromXml;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.FIELD)
public class OfferSeedDto {
    @XmlElement
    @Positive
    private double price;
    @XmlElement
    private AgentNameSeedDto agent;
    @XmlElement
    private ApartmentIdSeedDto apartment;
    @XmlElement
    private String publishedOn;

    public OfferSeedDto() {
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public AgentNameSeedDto getAgent() {
        return agent;
    }

    public void setAgent(AgentNameSeedDto agent) {
        this.agent = agent;
    }

    public ApartmentIdSeedDto getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentIdSeedDto apartment) {
        this.apartment = apartment;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }
}
