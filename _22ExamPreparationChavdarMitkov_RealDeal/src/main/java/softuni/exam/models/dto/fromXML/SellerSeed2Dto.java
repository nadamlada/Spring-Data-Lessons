package softuni.exam.models.dto.fromXML;

import softuni.exam.entity.enums.Rating;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
//валидациите на полетата или на гетъра
public class SellerSeed2Dto {
    //first-name -> нейминга от xml-a
    @XmlElement(name = "first-name")
    @Size(min = 2, max = 20)
    private String firstName;
    @XmlElement(name = "last-name")
    @Size(min = 2, max = 20)
    private String lastName;
    @XmlElement
    @Email
    private String email;
    @XmlElement
    @NotNull
    private Rating rating;
    @XmlElement
    //дали като е тримнато не е нъл
    @NotBlank
    private String town;

    public SellerSeed2Dto() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
