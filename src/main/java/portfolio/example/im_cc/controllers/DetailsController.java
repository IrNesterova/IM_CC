package portfolio.example.im_cc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/details")
public class DetailsController {

    @GetMapping
    public String getDetails() {
        return "details";
    }
}