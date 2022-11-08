package softuni.exam.models.dto.fromJSON;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class CarSeedDto {
    @Expose
    //и двете граници са инклудед
    @Size(min = 2, max = 20)
    private String make;
    @Expose
    @Size(min = 2, max = 20)
    private String model;
    @Expose
    @Positive
    Integer kilometers;
    //Взимаме датата като стринг, модел мапъра ще я конвертва
    @Expose
    private String registeredOn;

    public CarSeedDto() {
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getKilometers() {
        return kilometers;
    }

    public void setKilometers(Integer kilometers) {
        this.kilometers = kilometers;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }
}
