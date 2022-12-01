package hiberspring.domain.dto.fromXML.Products;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductSeedRoot1Dto {
    @XmlElement(name = "product")
    private List<ProductSeed2Dto> products;

    public ProductSeedRoot1Dto() {
    }

    public List<ProductSeed2Dto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductSeed2Dto> products) {
        this.products = products;
    }
}
