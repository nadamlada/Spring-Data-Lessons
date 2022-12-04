package softuni.exam.instagraphlite.models.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(nullable = false, unique = true, length = 18)
    private String username;
    @Column(nullable = false)
    private String password;
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    private Picture profilePicture;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Post>posts;

    public User() {
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Picture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Picture profilePicture) {
        this.profilePicture = profilePicture;
    }
}
