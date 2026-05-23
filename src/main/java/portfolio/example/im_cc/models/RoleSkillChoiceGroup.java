package portfolio.example.im_cc.models;

import jakarta.persistence.*;

@Entity
public class RoleSkillChoiceGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RoleInventoryChoiceGroup RoleChoice;

    @ManyToOne
    private Skill skill;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleInventoryChoiceGroup getRoleChoice() {
        return RoleChoice;
    }

    public void setRoleChoice(RoleInventoryChoiceGroup roleChoice) {
        RoleChoice = roleChoice;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }
}
