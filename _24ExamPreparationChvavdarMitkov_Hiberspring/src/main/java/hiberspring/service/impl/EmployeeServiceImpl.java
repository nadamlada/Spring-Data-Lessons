package hiberspring.service.impl;

import hiberspring.domain.dto.fromXML.Employees.EmployeeSeedRoot1Dto;
import hiberspring.domain.entity.Branch;
import hiberspring.domain.entity.Employee;
import hiberspring.domain.entity.EmployeeCard;
import hiberspring.repository.EmployeeRepository;
import hiberspring.service.BranchService;
import hiberspring.service.EmployeeCardService;
import hiberspring.service.EmployeeService;
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
import java.util.stream.Collectors;

import static hiberspring.common.GlobalConstants.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    //cascade = CascadeType.MERGE
    private final BranchService branchService;
    //cascade = CascadeType.MERGE
    private final EmployeeCardService employeeCardService;


    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               ValidationUtil validationUtil,
                               ModelMapper modelMapper,
                               XmlParser xmlParser,
                               BranchService branchService,
                               EmployeeCardService employeeCardService) {
        this.employeeRepository = employeeRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.branchService = branchService;
        this.employeeCardService = employeeCardService;
    }


    @Override
    public Boolean employeesAreImported() {
        return employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesXmlFile() throws IOException {
        return Files.readString(
                Path.of(EMPLOYEES_FILE_PATH)
        );
    }

    @Override
    public String importEmployees() throws JAXBException, FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();

        EmployeeSeedRoot1Dto employeeSeedRoot1Dto = xmlParser
                .fromFile(EMPLOYEES_FILE_PATH, EmployeeSeedRoot1Dto.class);

        employeeSeedRoot1Dto
                .getEmployees()
                .forEach(employeeSeed2Dto -> {
                    boolean isValid = validationUtil.isValid(employeeSeed2Dto);
                    Employee exists = employeeRepository
                            .findEmployeeByFirstNameAndLastNameAndPosition(
                                    employeeSeed2Dto.getFirstName(),
                                    employeeSeed2Dto.getLastName(),
                                    employeeSeed2Dto.getPosition()
                            );

                    if (isValid && exists == null) {
                        Employee employee = modelMapper.map(employeeSeed2Dto, Employee.class);
                        Branch branch = branchService
                                .getByStringName(employeeSeed2Dto.getBranch());
                        EmployeeCard employeeCard = employeeCardService
                                .getCardByNumber(employeeSeed2Dto.getCard());

                        employee.setBranch(branch);
                        employee.setCard(employeeCard);

                        stringBuilder.append(String.format(
                                SUCCESSFUL_IMPORT_MESSAGE + " %s",
                                "Employee",
                                employeeSeed2Dto.getFirstName(),
                                employeeSeed2Dto.getLastName()
                        ));

                        employeeRepository.saveAndFlush(employee);
                    } else {
                        stringBuilder.append(INCORRECT_DATA_MESSAGE);
                    }

                    stringBuilder.append(System.lineSeparator());
                });


        return stringBuilder.toString().trim();
    }

    @Override
    public String exportProductiveEmployees() {
        return
                employeeRepository
                        .findAllByBranchWithMoreThanOneProduct()
                        .stream()
                        .map(employee -> {
                            return String.format("%nName: %s %s%n" +
                                                 "Position: %s%n" +
                                                 "Card Number: %s%n",
                                    employee.getFirstName(),
                                    employee.getLastName(),
                                    employee.getPosition(),
                                    employee.getCard().getNumber()
                            );
                        })
                        .collect(Collectors.joining("-------------------------"));
    }
}
