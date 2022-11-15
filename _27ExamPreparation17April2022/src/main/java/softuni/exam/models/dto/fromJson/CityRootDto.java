package softuni.exam.models.dto.fromJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CityRootDto {
    @Expose
    @Size(min = 2, max = 60)
//    @SerializedName("city_name")
    @NotNull
    private String cityName;
    @Expose
    @Size(min = 2)
    private String description;
    @Expose
    @Min(value = 500)
    private int population;
    @Expose
    @NotNull
    private Long country;

    public CityRootDto() {
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public Long getCountry() {
        return country;
    }

    public void setCountry(Long country) {
        this.country = country;
    }
}
