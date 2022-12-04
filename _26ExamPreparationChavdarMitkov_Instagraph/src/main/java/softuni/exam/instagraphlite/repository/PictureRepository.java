package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.entity.Picture;

//ToDo find by връща опшънъл
@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
    Picture findByPath(String path);

//    boolean existsByPath(String path);
}
