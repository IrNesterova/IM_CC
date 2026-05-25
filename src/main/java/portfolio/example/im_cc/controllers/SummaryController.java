package portfolio.example.im_cc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import portfolio.example.im_cc.models.CharacterCreationModel;
import portfolio.example.im_cc.repositories.CombatActionRepository;
import portfolio.example.im_cc.repositories.InventoryRepository;
import portfolio.example.im_cc.repositories.MutationRepository;
import portfolio.example.im_cc.repositories.TalentRepository;
import portfolio.example.im_cc.services.SummaryServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/summary")
@SessionAttributes("characterCreation")
public class SummaryController {

    @Autowired private SummaryServiceImpl summaryService;
    @Autowired private TalentRepository talentRepository;
    @Autowired private InventoryRepository inventoryRepository;
    @Autowired private MutationRepository mutationRepository;
    @Autowired private CombatActionRepository combatActionRepository;

    @ModelAttribute("characterCreation")
    public CharacterCreationModel creationModel() {
        return new CharacterCreationModel();
    }

    @GetMapping
    public String getSummary(
            @ModelAttribute("characterCreation") CharacterCreationModel ccm,
            Model model
    ) {
        model.addAttribute("sheet", summaryService.build(ccm));

        // Talent names + descriptions
        List<String> allTalentNames = talentRepository
                .findAll(Sort.by(Sort.Direction.ASC, "name"))
                .stream().map(t -> t.getName()).collect(Collectors.toList());
        model.addAttribute("allTalentNames", allTalentNames);

        Map<String, String> talentDescMap = new HashMap<>();
        talentRepository.findAll().forEach(t -> {
            if (t.getName() != null)
                talentDescMap.put(t.getName(), t.getDescription() != null ? t.getDescription() : "");
        });
        model.addAttribute("talentDescMap", talentDescMap);

        // Inventory names
        List<String> allInventoryNames = inventoryRepository
                .findAll(Sort.by(Sort.Direction.ASC, "name"))
                .stream().map(i -> i.getName()).collect(Collectors.toList());
        model.addAttribute("allInventoryNames", allInventoryNames);

        // Mutation names + descriptions
        List<String> allMutationNames = mutationRepository
                .findAll(Sort.by(Sort.Direction.ASC, "name"))
                .stream().map(m -> m.getName()).collect(Collectors.toList());
        model.addAttribute("allMutationNames", allMutationNames);

        Map<String, String> mutationDescMap = new HashMap<>();
        mutationRepository.findAll().forEach(m -> {
            if (m.getName() != null)
                mutationDescMap.put(m.getName(), m.getDescription() != null ? m.getDescription() : "");
        });
        model.addAttribute("mutationDescMap", mutationDescMap);

        // Combat action names + descriptions
        List<String> allActionNames = combatActionRepository
                .findAll(Sort.by(Sort.Direction.ASC, "name"))
                .stream().map(a -> a.getName()).collect(Collectors.toList());
        model.addAttribute("allActionNames", allActionNames);

        Map<String, String> actionDescMap = new HashMap<>();
        combatActionRepository.findAll().forEach(a -> {
            if (a.getName() != null)
                actionDescMap.put(a.getName(), a.getDescription() != null ? a.getDescription() : "");
        });
        model.addAttribute("actionDescMap", actionDescMap);

        return "summary";
    }
}