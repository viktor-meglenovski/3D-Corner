package wp.threedcorner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wp.threedcorner.model.Comment;
import wp.threedcorner.model.Project;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    public List<Comment> findAllByProject(Project project);
}
