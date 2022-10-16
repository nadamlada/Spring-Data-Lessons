package softuni.exam.domain.dto.fromJson;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

//@XmlRootElement(name = "team")
//@XmlAccessorType(XmlAccessType.FIELD)
public class TeamSeedDto {
    //    @XmlElement(name = "name")
    @Expose
    @NotNull
    @Length(min = 3, max = 20)
    private String name;
    //    @XmlElement(name = "picture")
    @Expose
    @NotNull
    private PictureSeedDto picture;

    public TeamSeedDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PictureSeedDto getPicture() {
        return picture;
    }

    public void setPicture(PictureSeedDto picture) {
        this.picture = picture;
    }
}
