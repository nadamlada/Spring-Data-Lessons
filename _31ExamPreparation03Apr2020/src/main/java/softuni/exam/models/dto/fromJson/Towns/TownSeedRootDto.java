package softuni.exam.models.dto.fromJson.Towns;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class TownSeedRootDto {
    @Expose
    private String guide;
    @Expose
    @Positive
    private Integer population;
    @Expose
    @Size(min = 2)
    private String name;

    public TownSeedRootDto() {
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
