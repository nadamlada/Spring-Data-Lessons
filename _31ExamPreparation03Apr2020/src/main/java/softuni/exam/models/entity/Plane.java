package softuni.exam.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "planes")
public class Plane extends BaseEntity {
    @Column(nullable = false, name = "register_number", unique = true)
    private String registerNumber;
    @Column(nullable = false)
    private Integer capacity;
    @Column(nullable = false)
    private String airline;

    public Plane() {
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }
}
