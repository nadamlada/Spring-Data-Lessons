package softuni.exam.domain.dto.fromXml;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "team")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamSeed22Dto {
    @XmlElement(name = "name")
    @Length(min = 3, max = 20)
    private String name;
    @XmlElement(name = "picture")
    @NotNull
    private PictureSeed2Dto picture;

    public TeamSeed22Dto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PictureSeed2Dto getPicture() {
        return picture;
    }

    public void setPicture(PictureSeed2Dto picture) {
        this.picture = picture;
    }
}
