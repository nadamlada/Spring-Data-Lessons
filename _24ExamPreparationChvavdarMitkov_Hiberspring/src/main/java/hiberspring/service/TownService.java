package hiberspring.service;

//TODO ready

import hiberspring.domain.entity.Town;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface TownService {

    Boolean townsAreImported();

    String readTownsJsonFile() throws IOException;

    String importTowns(String townsFileContent) throws IOException;

    Town getTownByName(String name);

}
