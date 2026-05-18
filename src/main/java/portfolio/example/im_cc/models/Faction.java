package portfolio.example.im_cc.models;

import jakarta.persistence.*;

import java.util.List;

@Table
@Entity
public class Faction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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


    public List<Talent> getTalentList() {
        return talentList;
    }

    public void setTalentList(List<Talent> talentList) {
        this.talentList = talentList;
    }

    public List<Skill> getSkillList() {
        return skillList;
    }

    public void setSkillList(List<Skill> skillList) {
        this.skillList = skillList;
    }
}
