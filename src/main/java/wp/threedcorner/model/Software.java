package wp.threedcorner.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Software {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    @ManyToOne
    Image logo;

    public Software() {
    }

    public Software(String name, Image logo) {
        this.name = name;
        this.logo = logo;
    }
}
