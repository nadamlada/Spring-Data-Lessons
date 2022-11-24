package nadamlada.xmlProcessing.model.dto;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserWithOneSoldProductVewDto {
    @XmlAttribute(name = "fist-name")
    private String firstName;
    @XmlAttribute(name = "last-name")
    private String lastName;
    @XmlElement(name = "product")
    @XmlElementWrapper(name = "sold-products")
    private List<ProductWithBuyerViewDto> products;

    public UserWithOneSoldProductVewDto() {
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

    public List<ProductWithBuyerViewDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductWithBuyerViewDto> products) {
        this.products = products;
    }
}