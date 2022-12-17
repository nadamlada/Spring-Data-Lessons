package softuni.exam.domain.dto.fromJson.players;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotNull;

public class PictureSeedDto {
    @Expose
    @NotNull
    private String url;

    public PictureSeedDto() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}