package exam.model.dto.fromJson.customets;

import com.google.gson.annotations.Expose;
import exam.model.dto.fromXml.shops.TownNameDto;
import exam.model.entity.Town;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import java.time.LocalDate;

public class CustomerSeedRootDto {
    @Expose
    @Length(min = 2)
    private String firstName;
    @Expose
    @Length(min = 2)
    private String lastName;
    @Expose
    @Email
    private String email;
    @Expose
    private String registeredOn;
    @Expose
    private TownName2Dto town;

    public CustomerSeedRootDto() {
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

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }

    public TownName2Dto getTown() {
        return town;
    }

    public void setTown(TownName2Dto town) {
        this.town = town;
    }
}
