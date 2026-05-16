package portfolio.example.im_cc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import portfolio.example.im_cc.models.CharacterCreationModel;

import java.util.HashMap;
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
    public String saveOrigins(@ModelAttribute("characterCreation")CharacterCreationModel ccm, @RequestParam String originId){

        ccm.setOriginId(Long.parseLong(originId));
        return "redirect:/origins";
    }
}
