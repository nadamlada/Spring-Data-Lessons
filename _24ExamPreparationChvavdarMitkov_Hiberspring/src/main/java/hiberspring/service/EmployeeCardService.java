package hiberspring.service;

import java.io.FileNotFoundException;
import java.io.IOException;

// TODO ready
public interface EmployeeCardService {

    Boolean employeeCardsAreImported();

    String readEmployeeCardsJsonFile() throws IOException;

    String importEmployeeCards(String employeeCardsFileContent) throws IOException;

}
