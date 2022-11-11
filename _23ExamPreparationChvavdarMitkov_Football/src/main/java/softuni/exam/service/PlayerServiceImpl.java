package softuni.exam.service;


import org.springframework.stereotype.Service;



import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;



@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {


    @Override
    public String importPlayers() throws FileNotFoundException {
       return "";
    }

    @Override
    public boolean areImported() {
        return false;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return "";
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
       return "";
    }

    @Override
    public String exportPlayersInATeam() {
        return "";
    }


}
