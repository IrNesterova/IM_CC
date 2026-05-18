package portfolio.example.im_cc.models;

import jakarta.persistence.*;

@Entity
@Table
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "text")
    private String name;
}
