package softuni.exam.instagraphlite.util;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface ValidationUtil {
    <T> boolean isValid(T entity);
}

