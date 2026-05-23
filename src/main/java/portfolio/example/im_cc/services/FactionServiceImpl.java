package portfolio.example.im_cc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import portfolio.example.im_cc.models.*;
import portfolio.example.im_cc.repositories.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    FactionChoiceGroupRepository factionChoiceGroupRepository;
    @Autowired
    FactionInventoryChoiceRepository inventoryChoiceRepository;
    @Autowired
    FactionTalentChoiceRepository talentChoiceRepository;

    @Override
    public List<Faction> getAllFactions() {
        return factionRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

    }
    @Override
    public List<Faction> getAllFactionsWithAdds() {

        List<Faction> factionList =
                factionRepository.findAll(
                        Sort.by(Sort.Direction.ASC, "id")
                );

        for (Faction faction : factionList){

            // =====================================================
            // PRIMARY CHARACTERISTICS
            // =====================================================

            List<CharacteristicsFaction> cfP =
                    characteristicsFactionRepository
                            .findAllByFactionAndPrimaryChar(
                                    faction,
                                    true
                            );

            List<Characteristics> primaryChars =
                    new ArrayList<>();

            for (CharacteristicsFaction cf : cfP){
                primaryChars.add(
                        cf.getCharacteristics()
                );
            }

            faction.setPrimaryCharacteristics(
                    primaryChars
            );

            // =====================================================
            // SECONDARY CHARACTERISTICS
            // =====================================================

            List<CharacteristicsFaction> cfS =
                    characteristicsFactionRepository
                            .findAllByFactionAndPrimaryChar(
                                    faction,
                                    false
                            );

            List<Characteristics> secondaryChars =
                    new ArrayList<>();

            for (CharacteristicsFaction cf : cfS){
                secondaryChars.add(
                        cf.getCharacteristics()
                );
            }

            faction.setSecondaryCharacteristics(
                    secondaryChars
            );

            // =====================================================
            // TALENTS
            // =====================================================

            List<FactionTalent> ft =
                    factionTalentRepository
                            .findFactionTalentsByFaction(
                                    faction
                            );

            List<Talent> talents =
                    new ArrayList<>();

            for (FactionTalent tc : ft){
                talents.add(tc.getTalent());
            }

            faction.setTalentList(talents);

            // =====================================================
            // SKILLS
            // =====================================================

            List<SkillFactions> skillFactionsList =
                    skillFactionRepository
                            .findSkillFactionsByFaction(
                                    faction
                            );

            List<Skill> skills =
                    new ArrayList<>();

            for (SkillFactions sf : skillFactionsList){
                skills.add(sf.getSkill());
            }

            faction.setSkillList(skills);

            // =====================================================
            // INVENTORY
            // =====================================================

            List<FactionInventory> factionInventoryList =
                    factionInventoryRepository
                            .findFactionInventoriesByFaction(
                                    faction
                            );

            List<Inventory> inventoryList =
                    new ArrayList<>();

            for (FactionInventory fi : factionInventoryList){
                inventoryList.add(fi.getInventory());
            }

            faction.setInventoryList(inventoryList);

            // =====================================================
            // CHOICE GROUPS
            // =====================================================

            List<FactionChoiceGroup> groups =
                    factionChoiceGroupRepository
                            .findByFaction(faction);

            for (FactionChoiceGroup group : groups){

                // =========================================
                // CREATE OPTION MAP
                // =========================================

                Map<Long, ChoiceOption> optionMap =
                        new LinkedHashMap<>();

                // =========================================
                // TALENT CHOICES
                // =========================================

                List<FactionTalentChoice> talentChoices =
                        talentChoiceRepository
                                .findByFactionChoiceGroup(group);

                for (FactionTalentChoice tc : talentChoices){

                    Long optionId =
                            tc.getOption_id();

                    if(!optionMap.containsKey(optionId)){

                        ChoiceOption option =
                                new ChoiceOption();

                        option.setId(optionId);

                        option.setTalents(
                                new ArrayList<>()
                        );

                        option.setInventory(
                                new ArrayList<>()
                        );

                        optionMap.put(
                                optionId,
                                option
                        );
                    }

                    optionMap.get(optionId)
                            .getTalents()
                            .add(tc.getTalent());
                }

                // =========================================
                // INVENTORY CHOICES
                // =========================================

                List<FactionInventoryChoice> inventoryChoices =
                        inventoryChoiceRepository
                                .findByFactionChoiceGroup(group);

                for (FactionInventoryChoice fic : inventoryChoices){

                    Long optionId =
                            fic.getId();

                    if(!optionMap.containsKey(optionId)){

                        ChoiceOption option =
                                new ChoiceOption();

                        option.setId(optionId);

                        option.setTalents(
                                new ArrayList<>()
                        );

                        option.setInventory(
                                new ArrayList<>()
                        );

                        optionMap.put(
                                optionId,
                                option
                        );
                    }

                    optionMap.get(optionId)
                            .getInventory()
                            .add(fic.getInventory());
                }

                // =========================================
                // SET OPTIONS
                // =========================================

                group.setOptions(
                        new ArrayList<>(
                                optionMap.values()
                        )
                );
            }

            faction.setChoiceGroups(groups);
        }

        return factionList;
    }
    @Override
    public Faction getById(Long id) {
        return factionRepository.getFactionById(id);

    }

    @Override
    public Faction getFactionWithAdds(Long id){
        Faction faction =
                factionRepository.findById(id)
                        .orElseThrow();

        // =========================================
        // PRIMARY CHARACTERISTICS
        // =========================================

        List<CharacteristicsFaction> cfP =
                characteristicsFactionRepository
                        .findAllByFactionAndPrimaryChar(
                                faction,
                                true
                        );

        List<Characteristics> primary =
                new ArrayList<>();

        for(CharacteristicsFaction cf : cfP){

            primary.add(
                    cf.getCharacteristics()
            );
        }

        faction.setPrimaryCharacteristics(primary);

        // =========================================
        // SECONDARY CHARACTERISTICS
        // =========================================

        List<CharacteristicsFaction> cfS =
                characteristicsFactionRepository
                        .findAllByFactionAndPrimaryChar(
                                faction,
                                false
                        );

        List<Characteristics> secondary =
                new ArrayList<>();

        for(CharacteristicsFaction cf : cfS){

            secondary.add(
                    cf.getCharacteristics()
            );
        }

        faction.setSecondaryCharacteristics(
                secondary
        );

        // =========================================
        // SKILLS
        // =========================================

        List<SkillFactions> skillRows =
                skillFactionRepository
                        .findSkillFactionsByFaction(
                                faction
                        );

        List<Skill> skills =
                new ArrayList<>();

        for(SkillFactions sf : skillRows){

            skills.add(sf.getSkill());
        }

        faction.setSkillList(skills);

        // =========================================
        // TALENTS
        // =========================================

        List<FactionTalent> talentRows =
                factionTalentRepository
                        .findFactionTalentsByFaction(
                                faction
                        );

        List<Talent> talents =
                new ArrayList<>();

        for(FactionTalent ft : talentRows){

            talents.add(ft.getTalent());
        }

        faction.setTalentList(talents);

        // =========================================
        // INVENTORY
        // =========================================

        List<FactionInventory> inventoryRows =
                factionInventoryRepository
                        .findFactionInventoriesByFaction(
                                faction
                        );

        List<Inventory> inventory =
                new ArrayList<>();

        for(FactionInventory fi : inventoryRows){

            inventory.add(fi.getInventory());
        }

        faction.setInventoryList(inventory);

        // =========================================
        // CHOICE GROUPS
        // =========================================

        List<FactionChoiceGroup> groups =
                factionChoiceGroupRepository
                        .findByFaction(faction);

        faction.setChoiceGroups(groups);

        return faction;
    }
}
