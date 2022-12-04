package softuni.exam.instagraphlite.models.dto.fromJson;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.*;

public class PictureSeedDto {
    @Expose
    @NotNull
    private String path;
    @Expose
    @DecimalMin("500")
    @DecimalMax("60000")
    private double size;

    public PictureSeedDto() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
