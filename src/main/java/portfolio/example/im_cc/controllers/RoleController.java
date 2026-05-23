package portfolio.example.im_cc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import portfolio.example.im_cc.services.RoleServiceImpl;

@Controller
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleServiceImpl roleService;

    @GetMapping
    public String getRoles(Model model) {
        model.addAttribute("allRoles", roleService.getAllRolesWithAdds());
        return "roles";
    }
}