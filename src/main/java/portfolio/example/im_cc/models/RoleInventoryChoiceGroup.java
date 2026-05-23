package portfolio.example.im_cc.models;

import jakarta.persistence.*;
import jdk.jfr.Unsigned;

@Entity
public class RoleInventoryChoiceGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RoleInventoryChoiceGroup roleChoice;
    @ManyToOne
    private Inventory inventory;

    public RoleInventoryChoiceGroup getRoleChoice() {
        return roleChoice;
    }

    public void setRoleChoice(RoleInventoryChoiceGroup roleChoice) {
        this.roleChoice = roleChoice;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
