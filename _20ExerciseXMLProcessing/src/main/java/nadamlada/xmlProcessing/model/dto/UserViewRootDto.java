package nadamlada.xmlProcessing.model.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserViewRootDto {
    @XmlElement(name = "user")
    private List<UserWithOneSoldProductVewDto> userWithOneSoldProductVewDtos;

    public UserViewRootDto() {
    }

    public List<UserWithOneSoldProductVewDto> getUserWithOneSoldProductVewDtos() {
        return userWithOneSoldProductVewDtos;
    }

    public void setUserWithOneSoldProductVewDtos(List<UserWithOneSoldProductVewDto> userWithOneSoldProductVewDtos) {
        this.userWithOneSoldProductVewDtos = userWithOneSoldProductVewDtos;
    }
}
