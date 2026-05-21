package portfolio.example.im_cc.models;

import jakarta.persistence.*;

@Entity
public class FactionInventoryChoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private FactionInventoryChoice factionInventoryChoiceGroup;

    @ManyToOne
    private Inventory inventory;
}
