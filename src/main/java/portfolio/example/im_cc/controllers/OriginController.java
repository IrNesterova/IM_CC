package portfolio.example.im_cc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import portfolio.example.im_cc.models.CharacterCreationModel;
import portfolio.example.im_cc.models.Origin;
import portfolio.example.im_cc.repositories.OriginRepository;
import org.springframework.http.HttpStatus;
import portfolio.example.im_cc.services.OriginServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/origins")
public class OriginController {
    @Autowired
    private OriginRepository originRepository;

    @Autowired
    private OriginServiceImpl originService;
    //найти к чему плюс прибавляется
    @GetMapping
    public String getAllOrigins(Model model){
        model.addAttribute("listAllOrigins", originService.getAllOrigins());
        return "choose_your_origin";
    }

    @PostMapping("/select")
    public String selectOrigin(@ModelAttribute("characterCreation") CharacterCreationModel ccm, @RequestParam String originId){
        return "update";
    }


}
