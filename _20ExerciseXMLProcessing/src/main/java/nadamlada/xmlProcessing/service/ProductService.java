package nadamlada.xmlProcessing.service;

import nadamlada.xmlProcessing.model.dto.ProductSeedDto;
import nadamlada.xmlProcessing.model.dto.ProductViewRootDto;

import java.util.List;

public interface ProductService {
    long getCount();

    void seedProducts(List<ProductSeedDto> products);

    ProductViewRootDto findProductsInRangeWithNoBuyer();
}
