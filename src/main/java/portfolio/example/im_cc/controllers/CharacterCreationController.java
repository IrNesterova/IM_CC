package portfolio.example.im_cc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import portfolio.example.im_cc.models.CharacterCreationModel;
import portfolio.example.im_cc.models.Characteristics;
import portfolio.example.im_cc.models.Faction;
import portfolio.example.im_cc.services.CharacteristicsServiceImpl;
import portfolio.example.im_cc.services.FactionServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/character")
@SessionAttributes("characterCreation")
public class CharacterCreationController {
    @Autowired
    public FactionServiceImpl factionService;
    @Autowired
    public CharacteristicsServiceImpl characteristicsService;
    @ModelAttribute("characterCreation")
    public CharacterCreationModel creationModel(){
        return new CharacterCreationModel();
    }

    @PostMapping("/characteristics")
    public String saveCharacteristics(@ModelAttribute("characterCreation")CharacterCreationModel ccm, @RequestParam Map<String, String> params){

        Map<String, String> characteristics = new HashMap<>(params);
        ccm.setCharacteristics(characteristics);
        return "redirect:/origins";
    }

    @PostMapping("/origins")
    public String saveOrigins(@ModelAttribute("characterCreation")CharacterCreationModel ccm, @RequestParam Map<String, String> params){

        ccm.setOriginId(Long.valueOf(params.get("originId")));
        Map<String, String> chars = ccm.getCharacteristics();

        String characteristic = chars.get(params.get("secondaryChar_ID"));
        characteristic = String.valueOf(Integer.parseInt(characteristic) + 5);
        chars.replace(params.get("secondaryChar_ID"), characteristic);
        ccm.setCharacteristics(chars);
        return "redirect:/factions";
    }

    @PostMapping("/factions")
    public String saveFactions(

            @ModelAttribute("characterCreation")
            CharacterCreationModel ccm,

            @RequestParam Map<String, String> params

    ){

        // =====================================================
        // FACTION
        // =====================================================

        Long factionId =
                Long.valueOf(
                        params.get("factionId")
                );

        ccm.setFactionId(factionId);

        Faction faction =
                factionService.getFactionWithAdds(factionId);

        // =====================================================
        // CHARACTERISTICS
        // =====================================================
        System.out.println("Characteristics before:");
        System.out.println(ccm.getCharacteristics());
        Map<String, String> chars =
                ccm.getCharacteristics();

        // -----------------------------
        // PRIMARY +5
        // -----------------------------

        for (Characteristics characteristic :
                faction.getPrimaryCharacteristics()) {

            String current =
                    chars.get(
                            characteristic.getName()
                    );

            if(current != null){

                int updated =
                        Integer.parseInt(current) + 5;

                chars.put(
                        characteristic.getName(),
                        String.valueOf(updated)
                );
            }
        }

        // -----------------------------
        // SECONDARY +5
        // -----------------------------

        String secondaryId =
                params.get(
                        "selectedSecondaryCharacteristicId"
                );

        if(secondaryId != null){

            Characteristics secondary =
                    characteristicsService.getById(
                            Long.valueOf(secondaryId)
                    );

            String current =
                    chars.get(
                            secondary.getName()
                    );

            if(current != null){

                int updated =
                        Integer.parseInt(current) + 5;

                chars.put(
                        secondary.getName(),
                        String.valueOf(updated)
                );
            }
        }

        ccm.setCharacteristics(chars);

        // =====================================================
        // SKILL ADVANCES
        // =====================================================

        Map<Long, Integer> advances = new HashMap<>();
        for (String key: params.keySet()){
            if (key.startsWith("skill_")){
                Long skillId = Long.valueOf(key.replace("skill_",""));
                Integer value = Integer.valueOf(params.get(key));
                advances.put(skillId, value);
            }
        }
        ccm.setFactionSkillAdvances(advances);
        // =====================================================
        // CHOICE GROUPS
        // =====================================================

        Map<Long, Long> choices =
                new HashMap<>();

        for(String key : params.keySet()){

            if(key.startsWith("choiceGroup_")){

                Long groupId =
                        Long.valueOf(
                                key.replace(
                                        "choiceGroup_",
                                        ""
                                )
                        );

                Long optionId =
                        Long.valueOf(
                                params.get(key)
                        );

                choices.put(groupId, optionId);
            }
        }

        ccm.setFactionChoices(choices);

        return "redirect:/roles";
    }

    @PostMapping("/roles")
    public String saveRoles(
            @ModelAttribute("characterCreation") CharacterCreationModel ccm,
            @RequestParam Map<String, String> params
    ) {
        ccm.setRoleId(Long.valueOf(params.get("roleId")));

        Map<Long, List<Long>> roleChoices = new HashMap<>();

        for (String key : params.keySet()) {
            if (key.startsWith("roleChoice_")) {
                Long groupId = Long.valueOf(key.replace("roleChoice_", ""));
                String[] values = params.get(key).split(",");
                List<Long> selected = new ArrayList<>();
                for (String v : values) {
                    if (!v.isBlank()) selected.add(Long.valueOf(v.trim()));
                }
                roleChoices.put(groupId, selected);
            }
        }

        ccm.setRoleChoices(roleChoices);

        return "redirect:/details";
    }

    @PostMapping("/details")
    public String saveDetails(
            @ModelAttribute("characterCreation") CharacterCreationModel ccm,
            @RequestParam Map<String, String> params
    ) {
        ccm.setCharacterName(params.get("characterName"));
        ccm.setAge(params.get("age"));
        ccm.setEyeType(params.get("eyeType"));
        ccm.setHairColor(params.get("hairColor"));
        ccm.setHairStyle(params.get("hairStyle"));
        ccm.setHeight(params.get("height"));
        ccm.setDistinguishingFeatures(params.get("distinguishingFeatures"));
        ccm.setShortTermGoal(params.get("shortTermGoal"));
        ccm.setLongTermGoal(params.get("longTermGoal"));
        ccm.setConnections(params.get("connections"));
        return "redirect:/summary";
    }
}
