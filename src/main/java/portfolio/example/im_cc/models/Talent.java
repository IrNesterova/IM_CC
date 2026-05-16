package portfolio.example.im_cc.models;

import jakarta.persistence.*;

@Entity
@Table
public class Talent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
