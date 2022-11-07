package softuni.exam.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "offers")
public class Offer extends BaseEntity {
    @Column
    private BigDecimal price;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column
    private boolean hasGotStatus;
    @Column(name = "added_on")
    private LocalDateTime addedOn;
    @ManyToOne
    private Car car;
    @ManyToOne
    private Seller seller;
    @ManyToMany
    private Set<Picture>pictures;

    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHasGotStatus() {
        return hasGotStatus;
    }

    public void setHasGotStatus(boolean hasGotStatus) {
        this.hasGotStatus = hasGotStatus;
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
    }

    public Offer() {
    }
}
