package hiberspring.domain.dto.fromJson;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotNull;

public class EmployeeCardSeedDto {
    @Expose
    @NotNull
    private String number;

    public EmployeeCardSeedDto() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
