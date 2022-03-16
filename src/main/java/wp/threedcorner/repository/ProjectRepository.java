package wp.threedcorner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wp.threedcorner.model.Project;
import wp.threedcorner.model.User;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    public List<Project> findAllByAuthor(User author);
}
