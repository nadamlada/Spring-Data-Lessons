package softuni.exam.domain.dto.fromJson;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotNull;

//@XmlRootElement(name = "picture")
//@XmlAccessorType(XmlAccessType.FIELD)
public class PictureSeedDto {
    //    @XmlElement(name = "url")
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