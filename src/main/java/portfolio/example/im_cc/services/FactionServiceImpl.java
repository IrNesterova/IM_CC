package portfolio.example.im_cc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import portfolio.example.im_cc.models.*;
import portfolio.example.im_cc.repositories.*;

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
    @Autowired
    FactionInventoryRepository factionInventoryRepository;
    @Autowired
    CharacteristicsFactionRepository characteristicsFactionRepository;

    @Override
    public List<Faction> getAllFactions() {
        return factionRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

    }

    @Override
    public List<Faction> getAllFactionsWithAdds() {

        List<Faction> factionList = factionRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        for (Faction faction : factionList){
            List<CharacteristicsFaction> cfP = characteristicsFactionRepository.findAllByFactionAndPrimaryChar(faction, true);
            List<Characteristics> cfPP = new ArrayList<>();
            for (CharacteristicsFaction cf : cfP){
                cfPP.add(cf.getCharacteristics());
            }

            faction.setPrimaryCharacteristics(cfPP);
            List<Characteristics> cfPS = new ArrayList<>();
           // cfPP.clear();
            List<CharacteristicsFaction> cfS = characteristicsFactionRepository.findAllByFactionAndPrimaryChar(faction, false);

            for (CharacteristicsFaction cf : cfS){
                cfPS.add(cf.getCharacteristics());
            }
            faction.setSecondaryCharacteristics(cfPS);

            List<FactionTalent> ft = factionTalentRepository.findFactionTalentsByFaction(faction);

            List<Talent> talents = new ArrayList<>();
            for (FactionTalent tc : ft){
                talents.add(tc.getTalent());
            }

            faction.setTalentList(talents);
            List<SkillFactions> skillFactionsList = skillFactionRepository.findSkillFactionsByFaction(faction);

            List<Skill> skills = new ArrayList<>();
            for (SkillFactions sf : skillFactionsList){
                skills.add(sf.getSkill());
            }

            faction.setSkillList(skills);

            List<FactionInventory> factionInventoryList = factionInventoryRepository.findFactionInventoriesByFaction(faction);
            List<Inventory> inventoryList = new ArrayList<>();

            for (FactionInventory fi : factionInventoryList){
                inventoryList.add(fi.getInventory());
            }
            faction.setInventoryList(inventoryList);
        }
        return factionList;

    }

    @Override
    public Faction getById(Long id) {
        return factionRepository.getFactionById(id);

    }
}
