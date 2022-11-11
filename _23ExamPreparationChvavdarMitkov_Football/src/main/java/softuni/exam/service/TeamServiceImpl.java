package softuni.exam.service;


import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;


@Service
@Transactional
public class TeamServiceImpl implements TeamService {



    @Override
    
    public String importTeams() throws JAXBException, FileNotFoundException {
       return "";
    }

    @Override
    public boolean areImported() {
        return false;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
        return "";
    }

}
