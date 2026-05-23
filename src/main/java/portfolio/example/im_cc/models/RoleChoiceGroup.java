package portfolio.example.im_cc.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

@Entity
public class RoleChoiceGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer choicesRequired;

    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ChoiceType choiceType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getChoicesRequired() {
        return choicesRequired;
    }

    public void setChoicesRequired(Integer choicesRequired) {
        this.choicesRequired = choicesRequired;
    }
}
