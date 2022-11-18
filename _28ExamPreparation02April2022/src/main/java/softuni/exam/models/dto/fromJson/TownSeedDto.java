package softuni.exam.models.dto.fromJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class TownSeedDto {
    @Expose
    @Size(min = 2)
    @NotNull
    private String townName;
    @Expose
    @Positive
    @NotNull
    private int population;

    public TownSeedDto() {
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        townName = townName;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
