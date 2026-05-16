package portfolio.example.im_cc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import portfolio.example.im_cc.models.CharacterCreationModel;
import portfolio.example.im_cc.models.Characteristics;
import portfolio.example.im_cc.models.Origin;
import portfolio.example.im_cc.repositories.CharacteristicsRepository;
import portfolio.example.im_cc.repositories.OriginRepository;
import portfolio.example.im_cc.services.CharacteristicsService;
import portfolio.example.im_cc.services.CharacteristicsServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/characteristics")
public class CharacteristicsController {
    @Autowired
    private CharacteristicsServiceImpl CharacteristicsServiceImpl;
    @Autowired
    private CharacteristicsService characteristicsService;

    @GetMapping
    public String getAllCharacteristics(Model model){
        model.addAttribute("allCharacteristicsList", CharacteristicsServiceImpl.getAllCharacteristics());
        return "newcharacteristics";
    }
    @GetMapping("/addnew")
    public String addNewCharacteristics(Model model) {
        Characteristics characteristics = new Characteristics();
        model.addAttribute("characteristics", characteristics);
        return "newcharacteristics";
    }

    @PostMapping("/save")
    public String saveCharacteristics(Characteristics characteristics) {
        Characteristics chars = characteristicsService.getById(characteristics.getId());
        chars.setName(characteristics.getName());
        chars.setDescription(characteristics.getDescription());
        chars.setFull_name(characteristics.getFull_name());
        CharacteristicsServiceImpl.save(chars);
        return "redirect:/characteristics";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        Characteristics characteristics = CharacteristicsServiceImpl.getById(id);
        model.addAttribute("characteristics", characteristics);
        return "update";
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteThroughId(@PathVariable Long id) {
        CharacteristicsServiceImpl.deleteById(id);
        return "redirect:/characteristics";

    }



}
