package portfolio.example.im_cc.models;

import jakarta.persistence.*;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Entity
public class RoleTalentChoiceGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RoleInventoryChoiceGroup roleChoice;
    @ManyToOne
    private Talent talent;

    public Talent getTalent() {
        return talent;
    }

    public void setTalent(Talent talent) {
        this.talent = talent;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public RoleInventoryChoiceGroup getRoleChoice() {
        return roleChoice;
    }

    public void setRoleChoice(RoleInventoryChoiceGroup roleChoice) {
        this.roleChoice = roleChoice;
    }
}
