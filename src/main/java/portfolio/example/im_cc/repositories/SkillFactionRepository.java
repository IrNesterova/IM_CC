package portfolio.example.im_cc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.example.im_cc.models.SkillFactions;

public interface SkillFactionRepository extends JpaRepository<SkillFactions, Long> {
}
