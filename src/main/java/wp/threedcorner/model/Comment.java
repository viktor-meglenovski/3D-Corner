package wp.threedcorner.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String content;
    LocalDateTime created;
    @ManyToOne
    Project project;
    @ManyToOne
    User user;

    public Comment() {
    }

    public Comment(String content, LocalDateTime created, Project project, User user) {
        this.content = content;
        this.created = created;
        this.project = project;
        this.user = user;
    }
}
