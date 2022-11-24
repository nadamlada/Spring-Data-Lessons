package nadamlada.xmlProcessing;

import jakarta.xml.bind.JAXBException;
import nadamlada.xmlProcessing.model.dto.*;
import nadamlada.xmlProcessing.service.CategoryService;
import nadamlada.xmlProcessing.service.ProductService;
import nadamlada.xmlProcessing.service.UserService;
import nadamlada.xmlProcessing.util.XmlParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    public static final String RESOURCES_FOLDER_PATH = "src/main/resources/files/";
    public static final String OUTPUT_FOLDER_PATH = "src/main/resources/files/out/";

    public static final String CATEGORIES_FILE_NAME = "categories.xml";
    public static final String USERS_FILE_NAME = "users.xml";
    public static final String PRODUCTS_FILE_NAME = "products.xml";
    public static final String PRODUCTS_IN_RANGE_FILE_NAME = "products-in-range.xml";
    public static final String SOLD_PRODUCTS_FILE_NAME = "sold-products.xml";

    private final XmlParser xmlParser;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final BufferedReader bufferedReader;

    public CommandLineRunnerImpl(XmlParser xmlParser,
                                 CategoryService categoryService,
                                 UserService userService,
                                 ProductService productService) {
        this.xmlParser = xmlParser;
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();

        System.out.println("Please, select task number:");
        int number = Integer.parseInt(bufferedReader.readLine());

        switch (number) {
            case 1:
                productsInRange();
                break;
            case 2:
                usersWithSoldProducts();
                break;
        }
    }

    private void usersWithSoldProducts() throws JAXBException {
        UserViewRootDto userViewRootDto =
                userService.findUsersWithMoreThanOneSoldProducts();

        xmlParser.writeToFile(OUTPUT_FOLDER_PATH + SOLD_PRODUCTS_FILE_NAME,
                userViewRootDto);
    }

    private void productsInRange() throws JAXBException {
        //от базата в xml файл
        ProductViewRootDto productViewRootDto =
                productService.findProductsInRangeWithNoBuyer();

        xmlParser.writeToFile(OUTPUT_FOLDER_PATH + PRODUCTS_IN_RANGE_FILE_NAME,
                productViewRootDto);
    }

    private void seedData() throws JAXBException, FileNotFoundException {
        //от xml файл в базата
        if (categoryService.getEntityCount() == 0) {
            CategorySeedRootDto categorySeedRootDto = xmlParser
                    .fromFile(RESOURCES_FOLDER_PATH + CATEGORIES_FILE_NAME,
                            CategorySeedRootDto.class
                    );

//        System.out.println();

            categoryService
                    .seedCategories(categorySeedRootDto.getCategories());
        }

        if (userService.getCount() == 0) {
            UserSeedRootDto userSeedRootDto = xmlParser
                    .fromFile(RESOURCES_FOLDER_PATH + USERS_FILE_NAME,
                            UserSeedRootDto.class
                    );

//            System.out.println();

            userService
                    .seedUsers(userSeedRootDto.getUsers());
        }

        if (productService.getCount() == 0) {
            ProductSeedRootDto productSeedRootDto = xmlParser
                    .fromFile(RESOURCES_FOLDER_PATH + PRODUCTS_FILE_NAME,
                            ProductSeedRootDto.class
                    );

//        System.out.println();

            productService
                    .seedProducts(productSeedRootDto.getProducts());
        }
    }
}
