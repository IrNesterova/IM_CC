package portfolio.example.im_cc.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "text")
    private String name;
    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn
    private Characteristics characteristics;

    @Transient
    private List<Specialization> specializationList;
}
