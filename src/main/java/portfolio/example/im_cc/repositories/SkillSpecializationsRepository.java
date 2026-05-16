package portfolio.example.im_cc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import portfolio.example.im_cc.models.SkillSpecializations;

@Repository
public interface SkillSpecializationsRepository extends JpaRepository<SkillSpecializations, Long> {
}
