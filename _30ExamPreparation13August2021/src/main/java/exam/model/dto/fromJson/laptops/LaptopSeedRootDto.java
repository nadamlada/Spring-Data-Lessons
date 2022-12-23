package exam.model.dto.fromJson.laptops;

import com.google.gson.annotations.Expose;
import exam.model.entity.WarrantyType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class LaptopSeedRootDto {
    @Expose
    @Length(min = 8)
    private String macAddress;
    @Expose
    @Positive
    private Double cpuSpeed;
    @Expose
    @Min(value = 8)
    @Max(value = 128)
    private Integer ram;
    @Expose
    @Min(value = 128)
    @Max(value = 1024)
    private Integer storage;
    @Expose
    @Length(min = 10)
    private String description;
    @Expose
    @Positive
    private BigDecimal price;
    @Expose
    @NotNull
    private WarrantyType warrantyType;
    @Expose
    private ShopNameDto shop;


    public LaptopSeedRootDto() {
    }

    public Integer getRam() {
        return ram;
    }

    public void setRam(Integer ram) {
        this.ram = ram;
    }

    public Integer getStorage() {
        return storage;
    }

    public void setStorage(Integer storage) {
        this.storage = storage;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Double getCpuSpeed() {
        return cpuSpeed;
    }

    public void setCpuSpeed(Double cpuSpeed) {
        this.cpuSpeed = cpuSpeed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public WarrantyType getWarrantyType() {
        return warrantyType;
    }

    public void setWarrantyType(WarrantyType warrantyType) {
        this.warrantyType = warrantyType;
    }

    public ShopNameDto getShop() {
        return shop;
    }

    public void setShop(ShopNameDto shop) {
        this.shop = shop;
    }
}
