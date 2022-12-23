package exam.model.dto.fromJson.laptops;

import com.google.gson.annotations.Expose;

public class ShopNameDto {
    @Expose
    private String name;

    public ShopNameDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
