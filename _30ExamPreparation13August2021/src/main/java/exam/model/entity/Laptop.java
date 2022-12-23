package exam.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "laptops")
public class Laptop extends BaseEntity{
    @Column(name = "cpu_speed", nullable = false)
    private Double cpuSpeed;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(name = "mac_address",nullable = false, unique = true)
    private String macAddress;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Integer ram;
    @Column(nullable = false)
    private Integer storage;
    @Column(name = "warranty_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private WarrantyType warrantyType;
    @ManyToOne
    private Shop shop;

    public Laptop() {
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

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public WarrantyType getWarrantyType() {
        return warrantyType;
    }

    public void setWarrantyType(WarrantyType warrantyType) {
        this.warrantyType = warrantyType;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
