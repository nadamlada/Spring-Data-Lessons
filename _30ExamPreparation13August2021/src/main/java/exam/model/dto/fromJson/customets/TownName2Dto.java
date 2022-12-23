package exam.model.dto.fromJson.customets;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

public class TownName2Dto {
    @Expose
    @Length(min = 2)
    private String name;

    public TownName2Dto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
