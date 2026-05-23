package portfolio.example.im_cc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import portfolio.example.im_cc.models.*;
import portfolio.example.im_cc.repositories.*;

import java.util.ArrayList;
import java.util.List;

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

            role.setInventoryList(inventoryList);

            // =====================================================
            // CHOICE GROUPS
            // =====================================================

            List<RoleChoiceGroup> groups =
                    roleChoiceGroupRepository.findByRole(role);

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
                        group.setSpecializationOptions(specs);
                    }
                }
            }

            role.setChoiceGroups(groups);
        }

        return roles;
    }
}