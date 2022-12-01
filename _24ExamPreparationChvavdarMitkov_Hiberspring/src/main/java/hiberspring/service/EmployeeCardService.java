package hiberspring.service;

import hiberspring.domain.entity.EmployeeCard;

import java.io.FileNotFoundException;
import java.io.IOException;

// TODO ready
public interface EmployeeCardService {

    Boolean employeeCardsAreImported();

    String readEmployeeCardsJsonFile() throws IOException;

    String importEmployeeCards(String employeeCardsFileContent) throws IOException;

    EmployeeCard getCardByNumber(String number);
}
