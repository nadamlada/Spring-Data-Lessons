package softuni.exam.domain.dto.fromXml.teams;

import org.hibernate.validator.constraints.Length;
import softuni.exam.domain.dto.fromXml.pictures.PictureSeedDto;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "team")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamSeedDto {
    @XmlElement(name = "name")
    @Length(min = 3, max = 20)
    private String name;
    @XmlElement(name = "picture")
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
