package portfolio.example.im_cc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import portfolio.example.im_cc.models.*;
import portfolio.example.im_cc.repositories.*;

import java.util.*;

@Service
public class SummaryServiceImpl {

    @Autowired OriginRepository originRepository;
    @Autowired FactionRepository factionRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired SkillRepository skillRepository;
    @Autowired SpecializationRepository specializationRepository;
    @Autowired SkillSpecializationsRepository skillSpecializationsRepository;
    @Autowired FactionTalentRepository factionTalentRepository;
    @Autowired FactionInventoryRepository factionInventoryRepository;
    @Autowired FactionChoiceGroupRepository factionChoiceGroupRepository;
    @Autowired FactionTalentChoiceRepository factionTalentChoiceRepository;
    @Autowired FactionInventoryChoiceRepository factionInventoryChoiceRepository;
    @Autowired RoleInventoryRepository roleInventoryRepository;
    @Autowired RoleChoiceGroupRepository roleChoiceGroupRepository;
    @Autowired RoleTalentChoiceGroupRepository roleTalentChoiceGroupRepository;
    @Autowired RoleInventoryChoiceGroupRepository roleInventoryChoiceGroupRepository;

    public CharacterSheetDTO build(CharacterCreationModel ccm) {
        CharacterSheetDTO dto = new CharacterSheetDTO();

        dto.setCharacterName(ccm.getCharacterName());
        dto.setAge(ccm.getAge());
        dto.setHeight(ccm.getHeight());
        dto.setEyeType(ccm.getEyeType());
        dto.setHairColor(ccm.getHairColor());
        dto.setHairStyle(ccm.getHairStyle());
        dto.setDistinguishingFeatures(ccm.getDistinguishingFeatures());
        dto.setShortTermGoal(ccm.getShortTermGoal());
        dto.setLongTermGoal(ccm.getLongTermGoal());
        dto.setConnections(ccm.getConnections());
        dto.setCharacteristics(ccm.getCharacteristics());

        if (ccm.getOriginId() != null) {
            originRepository.findById(ccm.getOriginId())
                .ifPresent(o -> dto.setOriginName(o.getName()));
        }
        if (ccm.getFactionId() != null) {
            factionRepository.findById(ccm.getFactionId())
                .ifPresent(f -> dto.setFactionName(f.getName()));
        }
        if (ccm.getRoleId() != null) {
            roleRepository.findById(ccm.getRoleId())
                .ifPresent(r -> dto.setRoleName(r.getName()));
        }

        // ── SKILLS ────────────────────────────────────────────────────
        Map<Long, Integer> combinedSkillAdvances = new HashMap<>();
        if (ccm.getFactionSkillAdvances() != null) {
            combinedSkillAdvances.putAll(ccm.getFactionSkillAdvances());
        }
        if (ccm.getRoleSkillAdvances() != null) {
            for (Map.Entry<Long, Integer> e : ccm.getRoleSkillAdvances().entrySet()) {
                combinedSkillAdvances.merge(e.getKey(), e.getValue(), Integer::sum);
            }
        }

        List<Skill> allSkills = skillRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<CharacterSheetDTO.SkillEntry> skills = new ArrayList<>();
        for (Skill skill : allSkills) {
            int advances = combinedSkillAdvances.getOrDefault(skill.getId(), 0);
            String charAbbr = (skill.getCharacteristics() != null) ? skill.getCharacteristics().getName() : "";
            skills.add(new CharacterSheetDTO.SkillEntry(skill.getName(), advances, charAbbr));
        }
        dto.setSkills(skills);

        // ── TALENTS & EQUIPMENT ───────────────────────────────────────
        List<String> talents = new ArrayList<>();
        List<String> equipment = new ArrayList<>();

        if (ccm.getFactionId() != null) {
            Faction faction = factionRepository.findById(ccm.getFactionId()).orElseThrow();

            factionTalentRepository.findFactionTalentsByFaction(faction)
                .forEach(ft -> talents.add(ft.getTalent().getName()));
            factionInventoryRepository.findFactionInventoriesByFaction(faction)
                .forEach(fi -> equipment.add(fi.getInventory().getName()));

            for (Map.Entry<Long, Long> entry : ccm.getFactionChoices().entrySet()) {
                FactionChoiceGroup group = factionChoiceGroupRepository
                    .findById(entry.getKey()).orElse(null);
                if (group == null) continue;
                Long optionId = entry.getValue();
                factionTalentChoiceRepository.findByFactionChoiceGroup(group).stream()
                    .filter(tc -> optionId.equals(tc.getOption_id()))
                    .forEach(tc -> talents.add(tc.getTalent().getName()));
                factionInventoryChoiceRepository.findById(optionId)
                    .ifPresent(fic -> equipment.add(fic.getInventory().getName()));
            }
        }

        if (ccm.getRoleId() != null) {
            Role role = roleRepository.findById(ccm.getRoleId()).orElseThrow();

            roleInventoryRepository.findByRole(role)
                .forEach(ri -> equipment.add(ri.getInventory().getName()));

            List<RoleChoiceGroup> groups = roleChoiceGroupRepository.findByRole(role);
            for (RoleChoiceGroup group : groups) {
                List<Long> selected = ccm.getRoleChoices().getOrDefault(group.getId(), List.of());
                if (selected.isEmpty()) continue;

                switch (group.getChoiceType()) {
                    case TALENT -> roleTalentChoiceGroupRepository
                        .findByRoleChoiceGroup(group).stream()
                        .filter(r -> selected.contains(r.getTalent().getId()))
                        .forEach(r -> talents.add(r.getTalent().getName()));
                    case INVENTORY -> roleInventoryChoiceGroupRepository
                        .findByRoleChoiceGroup(group).stream()
                        .filter(r -> selected.contains(r.getInventory().getId()))
                        .forEach(r -> equipment.add(r.getInventory().getName()));
                }
            }
        }

        dto.setTalents(talents);
        dto.setEquipment(equipment);

        // ── FATE POINTS ───────────────────────────────────────────────
        boolean fated = talents.stream().anyMatch(t -> t.equalsIgnoreCase("Fated"));
        dto.setFatePoints(fated ? 4 : 3);

        // ── SPECIALIZATIONS ───────────────────────────────────────────
        List<CharacterSheetDTO.SpecializationEntry> specs = new ArrayList<>();
        if (ccm.getRoleSpecAdvances() != null) {
            for (Map.Entry<Long, Integer> e : ccm.getRoleSpecAdvances().entrySet()) {
                if (e.getValue() <= 0) continue;
                specializationRepository.findById(e.getKey()).ifPresent(spec -> {
                    List<SkillSpecializations> links =
                        skillSpecializationsRepository.findBySpecializationIn(List.of(spec));
                    String skillName = "";
                    String charAbbr = "";
                    int parentSkillAdv = 0;
                    if (!links.isEmpty()) {
                        Skill parentSkill = links.get(0).getSkill();
                        skillName = parentSkill.getName();
                        charAbbr = (parentSkill.getCharacteristics() != null)
                            ? parentSkill.getCharacteristics().getName() : "";
                        parentSkillAdv = combinedSkillAdvances.getOrDefault(parentSkill.getId(), 0);
                    }
                    specs.add(new CharacterSheetDTO.SpecializationEntry(
                        spec.getName(), skillName, charAbbr, e.getValue(), parentSkillAdv));
                });
            }
        }
        specs.sort(Comparator.comparing(CharacterSheetDTO.SpecializationEntry::getName));
        dto.setSpecializations(specs);

        return dto;
    }
}