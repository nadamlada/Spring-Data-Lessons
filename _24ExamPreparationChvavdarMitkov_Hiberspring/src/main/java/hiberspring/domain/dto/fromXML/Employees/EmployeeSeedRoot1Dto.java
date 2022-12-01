package hiberspring.domain.dto.fromXML.Employees;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "employees")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeeSeedRoot1Dto {
    @XmlElement(name = "employee")
    private List<EmployeeSeed2Dto> employees;

    public EmployeeSeedRoot1Dto() {
    }

    public List<EmployeeSeed2Dto> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeSeed2Dto> employees) {
        this.employees = employees;
    }
}
