package hiberspring.domain.entity;

import javax.persistence.*;

@Entity
@Table(name = "branches")
public class Branch extends BaseEntity {
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Town town;

    public Branch() {
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
