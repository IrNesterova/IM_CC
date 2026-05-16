package portfolio.example.im_cc.models;

import jakarta.persistence.*;

@Table
@Entity
public class SkillFactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;

    }
    @ManyToOne
    @JoinColumn
    private Faction faction;
    @ManyToOne
    @JoinColumn
    private Skill skill;
}
