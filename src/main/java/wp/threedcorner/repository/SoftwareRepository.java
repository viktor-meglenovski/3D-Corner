package wp.threedcorner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wp.threedcorner.model.Software;

import java.util.List;
import java.util.Set;

@Repository
public interface SoftwareRepository extends JpaRepository<Software,Long> {
}
