package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.fromJson.customets.CustomerSeedRootDto;
import exam.model.entity.Customer;
import exam.model.entity.Town;
import exam.repository.CustomerRepository;
import exam.service.CustomerService;
import exam.service.TownService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static exam.paths.MyPaths.CUSTOMERS_FILES_PATH;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final TownService townService;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               ValidationUtil validationUtil,
                               ModelMapper modelMapper,
                               Gson gson,
                               TownService townService) {
        this.customerRepository = customerRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return customerRepository.count() >0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        return Files.readString(
                Path.of(CUSTOMERS_FILES_PATH)
        );
    }

    @Override
    public String importCustomers() throws IOException {
       StringBuilder stringBuilder = new StringBuilder();

        CustomerSeedRootDto[] customerSeedRootDtos = gson
                .fromJson(readCustomersFileContent(), CustomerSeedRootDto[].class);

        Arrays.stream(customerSeedRootDtos)
                .forEach(customerSeedRootDto -> {
                    boolean valid = validationUtil.isValid(customerSeedRootDto);
                    Customer exist = customerRepository.getCustomerByEmail(
                            customerSeedRootDto.getEmail()
                    );

                    if(valid && exist == null) {
                        Customer customer = modelMapper
                                .map(customerSeedRootDto, Customer.class);
                        Town town = townService.getTownByName(
                                customerSeedRootDto.getTown().getName()
                        );

                        stringBuilder.append(String.format(
                                "Successfully imported Customer %s %s - %s",
                                customerSeedRootDto.getFirstName(),
                                customerSeedRootDto.getLastName(),
                                customerSeedRootDto.getEmail()
                        ));

                        customer.setTown(town);
                        customerRepository.saveAndFlush(customer);
                    }else {
                        stringBuilder.append("Invalid Customer");
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }
}
