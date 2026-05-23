package portfolio.example.im_cc.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String name;

    @Transient
    private List<Inventory> inventoryList;

    @Transient
    private List<RoleChoiceGroup> choiceGroups;


}
