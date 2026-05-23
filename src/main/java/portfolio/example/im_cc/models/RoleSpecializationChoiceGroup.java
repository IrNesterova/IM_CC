package portfolio.example.im_cc.models;

import jakarta.persistence.*;

@Entity
public class RoleSpecializationChoiceGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private RoleInventoryChoiceGroup roleChoice;

    @ManyToOne
    private Specialization specialization;

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public RoleInventoryChoiceGroup getRoleChoice() {
        return roleChoice;
    }

    public void setRoleChoice(RoleInventoryChoiceGroup roleChoice) {
        this.roleChoice = roleChoice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
