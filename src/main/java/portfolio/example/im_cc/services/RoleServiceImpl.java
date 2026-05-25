package portfolio.example.im_cc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import portfolio.example.im_cc.models.*;
import portfolio.example.im_cc.repositories.*;

import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    RoleInventoryRepository roleInventoryRepository;
    @Autowired
    RoleChoiceGroupRepository roleChoiceGroupRepository;
    @Autowired
    RoleTalentChoiceGroupRepository roleTalentChoiceGroupRepository;
    @Autowired
    RoleInventoryChoiceGroupRepository roleInventoryChoiceGroupRepository;
    @Autowired
    RoleSkillChoiceGroupRepository roleSkillChoiceGroupRepository;
    @Autowired
    RoleSpecializationChoiceGroupRepository roleSpecializationChoiceGroupRepository;
    @Autowired
    portfolio.example.im_cc.repositories.SkillSpecializationsRepository skillSpecializationsRepository;

    @Override
    public List<Role> getAllRolesWithAdds() {

        List<Role> roles =
                roleRepository.findAll(
                        Sort.by(Sort.Direction.ASC, "id")
                );

        for (Role role : roles) {

            // =====================================================
            // FIXED INVENTORY
            // =====================================================

            List<RoleInventory> inventoryRows =
                    roleInventoryRepository.findByRole(role);

            List<Inventory> inventoryList = new ArrayList<>();
            for (RoleInventory ri : inventoryRows) {
                inventoryList.add(ri.getInventory());
            }
            inventoryList.sort(Comparator.comparing(Inventory::getId));
            role.setInventoryList(inventoryList);

            // =====================================================
            // CHOICE GROUPS
            // =====================================================

            List<RoleChoiceGroup> groups =
                    roleChoiceGroupRepository.findByRole(role);
            groups.sort(Comparator.comparing(RoleChoiceGroup::getId));

            for (RoleChoiceGroup group : groups) {

                switch (group.getChoiceType()) {

                    case TALENT -> {
                        List<RoleTalentChoiceGroup> rows =
                                roleTalentChoiceGroupRepository
                                        .findByRoleChoiceGroup(group);
                        List<Talent> talents = new ArrayList<>();
                        for (RoleTalentChoiceGroup r : rows) {
                            talents.add(r.getTalent());
                        }
                        talents.sort(Comparator.comparing(Talent::getId));
                        group.setTalentOptions(talents);
                    }

                    case INVENTORY -> {
                        List<RoleInventoryChoiceGroup> rows =
                                roleInventoryChoiceGroupRepository
                                        .findByRoleChoiceGroup(group);
                        List<Inventory> items = new ArrayList<>();
                        for (RoleInventoryChoiceGroup r : rows) {
                            items.add(r.getInventory());
                        }
                        items.sort(Comparator.comparing(Inventory::getId));
                        group.setInventoryOptions(items);
                    }

                    case SKILL -> {
                        List<RoleSkillChoiceGroup> rows =
                                roleSkillChoiceGroupRepository
                                        .findByRoleChoiceGroup(group);
                        List<Skill> skills = new ArrayList<>();
                        for (RoleSkillChoiceGroup r : rows) {
                            skills.add(r.getSkill());
                        }
                        skills.sort(Comparator.comparing(Skill::getId));
                        group.setSkillOptions(skills);
                    }

                    case SPECIALIZATION -> {
                        List<RoleSpecializationChoiceGroup> rows =
                                roleSpecializationChoiceGroupRepository
                                        .findByRoleChoiceGroup(group);
                        List<Specialization> specs = new ArrayList<>();
                        for (RoleSpecializationChoiceGroup r : rows) {
                            specs.add(r.getSpecialization());
                        }
                        specs.sort(Comparator.comparing(Specialization::getId));
                        group.setSpecializationOptions(specs);

                        // Build skill→specializations tree
                        List<SkillSpecializations> links =
                                skillSpecializationsRepository.findBySpecializationIn(specs);
                        Map<Skill, List<Specialization>> grouped = new LinkedHashMap<>();
                        for (SkillSpecializations link : links) {
                            grouped.computeIfAbsent(link.getSkill(), k -> new ArrayList<>())
                                   .add(link.getSpecialization());
                        }
                        List<Skill> skillsWithSpecs = new ArrayList<>();
                        for (Map.Entry<Skill, List<Specialization>> entry : grouped.entrySet()) {
                            Skill skill = entry.getKey();
                            List<Specialization> skillSpecs = entry.getValue();
                            skillSpecs.sort(Comparator.comparing(Specialization::getId));
                            skill.setSpecializationList(skillSpecs);
                            skillsWithSpecs.add(skill);
                        }
                        skillsWithSpecs.sort(Comparator.comparing(Skill::getId));
                        group.setSpecsBySkill(skillsWithSpecs);
                    }
                }
            }

            role.setChoiceGroups(groups);
        }

        return roles;
    }
}