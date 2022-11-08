package softuni.exam.models.dto.fromJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.validation.constraints.Size;

public class PictureSeedDto {
    @Expose
    //и двете граници са инклудед
    @Size(min = 2, max = 20)
    private String name;
    @Expose
    //взимаме го като стринг, модел мапъра ще конвъртне
    private String dateAndTime;
    @Expose
    @SerializedName("car")
    //ай ди на кар
    private Long carId;

    public PictureSeedDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }
}
