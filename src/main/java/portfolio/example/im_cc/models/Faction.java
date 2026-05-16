package portfolio.example.im_cc.models;

import jakarta.persistence.*;

import java.util.List;

@Table
@Entity
public class Faction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Column(columnDefinition = "text")
    private String name;

    @Transient
    private List<Talent> talentList;

    @Transient
    private List<Skill> skillList;
}
