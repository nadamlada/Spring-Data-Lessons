package softuni.exam.domain.dto.fromXml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "pictures")
@XmlAccessorType(XmlAccessType.FIELD)
public class PictureSeedRoot1Dto {
    @XmlElement(name = "picture")
    List<PictureSeed2Dto> pictures;

    public PictureSeedRoot1Dto() {
    }

    public List<PictureSeed2Dto> getPictures() {
        return pictures;
    }

    public void setPictures(List<PictureSeed2Dto> pictures) {
        this.pictures = pictures;
    }
}
