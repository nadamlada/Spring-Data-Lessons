package softuni.exam.service;

import org.springframework.stereotype.Service;


import javax.xml.bind.JAXBException;
import java.io.IOException;


@Service
public class PictureServiceImpl implements PictureService {


    @Override
    public String importPictures() throws IOException, JAXBException {
       return "";
    }

    @Override
    public boolean areImported() {
        return false;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {
        return "";
    }


}
