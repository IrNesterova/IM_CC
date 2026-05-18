package portfolio.example.im_cc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import portfolio.example.im_cc.models.*;
import portfolio.example.im_cc.repositories.FactionRepository;
import portfolio.example.im_cc.repositories.FactionTalentRepository;
import portfolio.example.im_cc.repositories.SkillFactionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class FactionServiceImpl implements FactionService {
    @Autowired
    FactionRepository factionRepository;
    @Autowired
    FactionTalentRepository factionTalentRepository;
    @Autowired
    SkillFactionRepository skillFactionRepository;

    @Override
    public List<Faction> getAllFactions() {
        return factionRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

    }

    @Override
    public List<Faction> getAllFactionsWithAdds() {

        List<Faction> factionList = factionRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        for (Faction faction : factionList){
            List<FactionTalent> ft = factionTalentRepository.findFactionTalentsByFaction(faction);

            List<Talent> talents = new ArrayList<>();
            for (FactionTalent tc : ft){
                talents.add(tc.getTalent());
            }

            faction.setTalentList(talents);
            List<SkillFactions> skillFactionsList = skillFactionRepository.findAll();

            List<Skill> skills = new ArrayList<>();
            for (SkillFactions sf : skillFactionsList){
                skills.add(sf.getSkill());
            }

            faction.setSkillList(skills);

        }
        return factionList;

    }

    @Override
    public Faction getById(Long id) {
        return factionRepository.getFactionById(id);

    }
}
