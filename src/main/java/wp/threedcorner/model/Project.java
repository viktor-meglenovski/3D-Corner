package wp.threedcorner.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
    @ManyToOne
    Image mainImage;
    @ManyToMany
    List<Image> images;
    @ManyToMany
    List<User> likes;
    @ManyToMany
    List<Software> software;
    @OneToMany
    List<Comment> comments;
    String location;
    public Project() {
    }

    public Project(String name, String description, LocalDateTime created, User author, Image mainImage, List<Image> images, List<User> likes,List<Software> software,String location) {
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
