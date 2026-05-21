package portfolio.example.im_cc.models;

import jakarta.persistence.*;

@Entity
public class FactionTalentChoiceGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private FactionInventoryChoice factionInventoryChoice;
    @ManyToOne
    private Talent talent;
}
