package softuni.exam.service;


import softuni.exam.models.entity.Country;

import java.io.IOException;

public interface CountryService {

    boolean areImported();

    String readCountriesFromFile() throws IOException;
	
	String importCountries() throws IOException;

    Country getCountryById(Long id);
}
