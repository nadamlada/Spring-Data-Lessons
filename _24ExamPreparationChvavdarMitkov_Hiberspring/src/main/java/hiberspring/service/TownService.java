package hiberspring.service;

//TODO ready

import java.io.FileNotFoundException;
import java.io.IOException;

public interface TownService {

    Boolean townsAreImported();

    String readTownsJsonFile() throws IOException;

    String importTowns(String townsFileContent) throws IOException;

}
