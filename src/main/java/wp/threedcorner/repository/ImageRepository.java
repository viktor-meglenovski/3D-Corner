package wp.threedcorner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wp.threedcorner.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
    Image findByLocationContaining(String s);
}
