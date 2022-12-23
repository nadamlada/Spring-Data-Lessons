package exam.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "towns")
public class Town extends BaseEntity{
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private Integer population;
    @Column(name = "travel_guide", nullable = false, columnDefinition = "TEXT")
    private String travelGuide;
    @OneToMany(mappedBy = "town", fetch = FetchType.EAGER)
    private List<Shop>shops;

    public Town() {
    }

    public List<Shop> getShops() {
        return shops;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public String getTravelGuide() {
        return travelGuide;
    }

    public void setTravelGuide(String travelGuide) {
        this.travelGuide = travelGuide;
    }
}
