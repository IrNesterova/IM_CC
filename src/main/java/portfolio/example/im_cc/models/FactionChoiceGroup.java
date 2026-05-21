package portfolio.example.im_cc.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class FactionChoiceGroup {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne
   private Faction faction;

   private Integer choicesRequired;

   @Transient
   private List<ChoiceGroup>  choices;
}
