package exam.model.dto.fromXml.shops;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
public class ShopSeedDto {
    @XmlElement
    @Length(min = 4)
    private String address;
    @XmlElement(name = "employee-count")
    @Min(value = 1)
    @Max(value = 50)
    private Integer employeeCount;
    @XmlElement
    @DecimalMin(value = "20000")
    private BigDecimal income;
    @XmlElement
    @Length(min = 4)
    private String name;
    @XmlElement(name = "shop-area")
    @Min(value = 150)
    private Integer shopArea;
    @XmlElement
    private TownNameDto town;

    public ShopSeedDto() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getShopArea() {
        return shopArea;
    }

    public void setShopArea(Integer shopArea) {
        this.shopArea = shopArea;
    }

    public TownNameDto getTown() {
        return town;
    }

    public void setTown(TownNameDto town) {
        this.town = town;
    }
}
