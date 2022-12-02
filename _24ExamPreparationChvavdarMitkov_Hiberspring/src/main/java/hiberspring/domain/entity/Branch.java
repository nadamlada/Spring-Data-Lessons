package hiberspring.domain.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "branches")
public class Branch extends BaseEntity {
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Town town;
    @OneToMany(mappedBy = "branch")
    private Set<Product> products;

    public Branch() {
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }
}
