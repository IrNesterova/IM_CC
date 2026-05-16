package portfolio.example.im_cc.models;

import jakarta.persistence.*;

@Table
@Entity
public class Faction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Column(columnDefinition = "text")
    private String name;


}
