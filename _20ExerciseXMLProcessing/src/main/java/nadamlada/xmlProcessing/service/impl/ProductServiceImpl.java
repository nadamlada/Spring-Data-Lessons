package nadamlada.xmlProcessing.service.impl;

import nadamlada.xmlProcessing.model.dto.ProductSeedDto;
import nadamlada.xmlProcessing.model.dto.ProductViewRootDto;
import nadamlada.xmlProcessing.model.dto.ProductWithSellerViewDto;
import nadamlada.xmlProcessing.model.entity.Product;
import nadamlada.xmlProcessing.repository.ProductRepository;
import nadamlada.xmlProcessing.service.CategoryService;
import nadamlada.xmlProcessing.service.ProductService;
import nadamlada.xmlProcessing.service.UserService;
import nadamlada.xmlProcessing.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final UserService userService;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository,
                              ModelMapper modelMapper,
                              ValidationUtil validationUtil,
                              UserService userService,
                              CategoryService categoryService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public long getCount() {
        return productRepository.count();
    }

    @Override
    public void seedProducts(List<ProductSeedDto> products) {
        products
                .stream()
                .filter(validationUtil::isValid)
                .map(productSeedDto -> {
                            Product product = modelMapper.map(productSeedDto, Product.class);
                            product.setSeller(userService.getRandomUser());

                            if (product.getPrice().compareTo(BigDecimal.valueOf(700L)) > 0) {
                                product.setBuyer(userService.getRandomUser());
                            }

                            product.setCategories(categoryService.getRandomCategories());

                            return product;
                        }
                )
                .forEach(productRepository::save);
    }

    @Override
    public ProductViewRootDto findProductsInRangeWithNoBuyer() {
        ProductViewRootDto productViewRootDto = new ProductViewRootDto();

        List<Product> searchedByPriceBetweenAndBuyerIsNull = productRepository.
                findAllByPriceBetweenAndBuyerIsNull(
                        BigDecimal.valueOf(500L),
                        BigDecimal.valueOf(1000L)
                );

        productViewRootDto
                .setProducts(searchedByPriceBetweenAndBuyerIsNull
                                .stream()
                                .map(product -> {
                                            ProductWithSellerViewDto productWithSellerViewDto =
                                                    modelMapper.map(product, ProductWithSellerViewDto.class);

                                            productWithSellerViewDto.setSeller(
                                                    String.format("%s %s",
                                                            product.getSeller().getFirstName(),
                                                            product.getSeller().getLastName()
                                                    )
                                            );

                                            return productWithSellerViewDto;
                                        }
                                )
                                .collect(Collectors.toList())
                );

        return productViewRootDto;
    }
}
