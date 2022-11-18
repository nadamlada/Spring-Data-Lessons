package softuni.exam.models.dto.fromJson;

import com.google.gson.annotations.Expose;
import softuni.exam.models.entity.Town;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AgentSeedDto {
    @Expose
    @NotNull
    @Size(min = 2)
    private String firstName;
    @Expose
    @NotNull
    @Size(min = 2)
    private String lastName;
    @Expose
    private Long id;
    @Expose
    @NotNull
    @Email
    private String email;

    public AgentSeedDto() {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
