package softuni.exam.models.entity;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Entity
@Table(name = "cities")
public class City extends BaseEntity {
    @Column(name = "city_name", length = 60, unique = true, nullable = false)
    private String cityName;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private int population;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Country country;

    public City() {
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

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
