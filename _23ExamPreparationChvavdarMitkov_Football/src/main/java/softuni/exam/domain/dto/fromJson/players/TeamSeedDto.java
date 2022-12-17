package softuni.exam.domain.dto.fromJson.players;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class TeamSeedDto {
    @Expose
    @NotNull
    @Length(min = 3, max = 20)
    private String name;
    @Expose
    @NotNull
    private PictureSeedDto picture;

    public TeamSeedDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PictureSeedDto getPicture() {
        return picture;
    }

    public void setPicture(PictureSeedDto picture) {
        this.picture = picture;
    }
}
