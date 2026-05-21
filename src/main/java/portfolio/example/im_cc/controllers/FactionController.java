package portfolio.example.im_cc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import portfolio.example.im_cc.services.CharacteristicsService;
import portfolio.example.im_cc.services.CharacteristicsServiceImpl;
import portfolio.example.im_cc.services.FactionServiceImpl;

@Controller
@RequestMapping("/factions")
public class FactionController {

    @Autowired
    private FactionServiceImpl factionService;


    @GetMapping
    public String getAllCharacteristics(Model model){
        model.addAttribute("allFactions", factionService.getAllFactionsWithAdds());
        return "factions";
    }

}
