package hiberspring.domain.entity;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    @Column(name = "clients")
    private String clients;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Branch branch;

    public Product() {
    }

    public String getClients() {
        return clients;
    }

    public void setClients(String clients) {
        this.clients = clients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}

