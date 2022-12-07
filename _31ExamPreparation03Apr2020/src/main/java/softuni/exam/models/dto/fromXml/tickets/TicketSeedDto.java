package softuni.exam.models.dto.fromXml.tickets;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
public class TicketSeedDto {
    @XmlElement(name = "serial-number")
    @Size(min = 2)
    private String serialNumber;
    @XmlElement
    @Positive
    private BigDecimal price;
    @XmlElement(name = "take-off")
    private String takeOff;
    @XmlElement(name = "from-town")
    private TownFromNameDto fromTown;
    @XmlElement(name = "to-town")
    private TownToNameDto toTown;
    @XmlElement
    private PassengerEmailDto passenger;
    @XmlElement
    private PlaneRegNumDto plane;

    public TicketSeedDto() {
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTakeOff() {
        return takeOff;
    }

    public void setTakeOff(String takeOff) {
        this.takeOff = takeOff;
    }

    public TownFromNameDto getFromTown() {
        return fromTown;
    }

    public void setFromTown(TownFromNameDto fromTown) {
        this.fromTown = fromTown;
    }

    public TownToNameDto getToTown() {
        return toTown;
    }

    public void setToTown(TownToNameDto toTown) {
        this.toTown = toTown;
    }

    public PassengerEmailDto getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerEmailDto passenger) {
        this.passenger = passenger;
    }

    public PlaneRegNumDto getPlane() {
        return plane;
    }

    public void setPlane(PlaneRegNumDto plane) {
        this.plane = plane;
    }
}
