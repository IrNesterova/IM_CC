package portfolio.example.im_cc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import portfolio.example.im_cc.models.CharacterCreationModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/character")
@SessionAttributes("characterCreation")
public class CharacterCreationController {

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
    public String saveFactions(@ModelAttribute("characterCreation")CharacterCreationModel ccm, @RequestParam Map<String, String> advances){


        return "redirect:/roles";
    }
}
