package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.entity.User;

import java.util.List;

//ToDo ready
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getByUsername(String username);

//    ако не са fetch
//    @Query("""
//            SELECT DISTINCT u
//            FROM User u
//            JOIN FETCH u.posts p
//            ORDER BY SIZE(p) DESC, u.id
//            """)
//    List<User> findAllByPostsOrderByCountOfPostsDescAndUserId();

    @Query("""
            SELECT u
            FROM User u
            ORDER BY SIZE(u.posts) DESC,u.id
            """)
    List<User> findAllByPostsOrderByCountOfPostsDescAndUserId();
}
