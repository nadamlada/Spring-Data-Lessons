package nadamlada.xmlProcessing.model.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductViewRootDto {
    @XmlElement(name = "product")
    private List<ProductWithSellerViewDto> products;

    public ProductViewRootDto() {
    }

    public List<ProductWithSellerViewDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductWithSellerViewDto> products) {
        this.products = products;
    }
}
