package softuni.exam.instagraphlite.models.dto.fromXml;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PostSeedDto {
    @XmlElement
    @Size(min = 21)
    private String caption;
    @XmlElement(name = "user")
    @NotNull
    private UserUserNameDto user;
    @XmlElement(name = "picture")
    @NotNull
    private PicturePathDto picture;

    public PostSeedDto() {
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public UserUserNameDto getUser() {
        return user;
    }

    public void setUser(UserUserNameDto user) {
        this.user = user;
    }

    public PicturePathDto getPicture() {
        return picture;
    }

    public void setPicture(PicturePathDto picture) {
        this.picture = picture;
    }
}
