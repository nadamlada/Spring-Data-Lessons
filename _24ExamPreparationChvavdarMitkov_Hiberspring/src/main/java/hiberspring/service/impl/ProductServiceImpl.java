package hiberspring.service.impl;

import hiberspring.common.GlobalConstants;
import hiberspring.domain.dto.fromXML.Products.ProductSeedRoot1Dto;
import hiberspring.domain.entity.Branch;
import hiberspring.domain.entity.Product;
import hiberspring.repository.ProductRepository;
import hiberspring.service.BranchService;
import hiberspring.service.ProductService;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static hiberspring.common.GlobalConstants.INCORRECT_DATA_MESSAGE;
import static hiberspring.common.GlobalConstants.PRODUCTS_FILE_PATH;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    //щом импотваш класа -> в ентити продукт на бранч (cascade = CascadeType.MERGE)
    private final BranchService branchService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              ValidationUtil validationUtil,
                              ModelMapper modelMapper,
                              XmlParser xmlParser,
                              BranchService branchService) {
        this.productRepository = productRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.branchService = branchService;
    }

    @Override
    public Boolean productsAreImported() {
        return productRepository.count() > 0;
    }

    @Override
    public String readProductsXmlFile() throws IOException {
        return Files.readString(
                Path.of(PRODUCTS_FILE_PATH)
        );
    }

    @Override
    public String importProducts() throws JAXBException, FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();

        ProductSeedRoot1Dto productSeedRoot1Dto = xmlParser
                .fromFile(PRODUCTS_FILE_PATH, ProductSeedRoot1Dto.class);

        productSeedRoot1Dto
                .getProducts()
                .forEach(productSeed2Dto -> {
                    boolean isValid = validationUtil.isValid(productSeed2Dto);
                    Product exists = productRepository
                            .findByName(productSeed2Dto.getName());

                    if (isValid && exists == null) {
                        Product product = modelMapper.map(productSeed2Dto, Product.class);

                        Branch branch = branchService.getByStringName(
                                productSeed2Dto.getBranch()
                        );

                        product.setBranch(branch);

                        stringBuilder.append(String.format(
                                GlobalConstants.SUCCESSFUL_IMPORT_MESSAGE,
                                "Product",
                                productSeed2Dto.getName()
                        ));

                        productRepository.saveAndFlush(product);
                    } else {
                        stringBuilder.append(INCORRECT_DATA_MESSAGE);
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }
}
