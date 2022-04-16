package wp.threedcorner.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    LocalDateTime created;
    @ManyToOne
    User author;
    @ManyToOne(cascade = CascadeType.ALL)
    Image mainImage;
    @ManyToMany(cascade = CascadeType.ALL)
    Set<Image> images;
    @ManyToMany
    Set<User> likes;
    @ManyToMany
    Set<Software> software;
    @OneToMany(mappedBy = "project")
    List<Comment> comments;
    String location;
    public Project() {
    }

    public Project(String name, String description, LocalDateTime created, User author, Image mainImage, Set<Image> images, Set<User> likes,Set<Software> software,String location) {
        this.name = name;
        this.description = description;
        this.created = created;
        this.author = author;
        this.mainImage = mainImage;
        this.images = images;
        this.likes = likes;
        this.software=software;
        this.location=location;
    }
}
