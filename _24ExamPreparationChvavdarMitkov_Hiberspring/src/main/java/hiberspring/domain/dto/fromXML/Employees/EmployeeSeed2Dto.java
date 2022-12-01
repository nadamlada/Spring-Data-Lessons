package hiberspring.domain.dto.fromXML.Employees;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeeSeed2Dto {
    @NotNull
    @XmlAttribute(name = "first-name")
    private String firstName;
    @NotNull
    @XmlAttribute(name = "last-name")
    private String lastName;
    @NotNull
    @XmlAttribute(name = "position")
    private String position;
    @NotNull
    @XmlElement
    private String card;
    @NotNull
    @XmlElement
    private String branch;

    public EmployeeSeed2Dto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
