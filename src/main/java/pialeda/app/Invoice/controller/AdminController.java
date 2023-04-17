package pialeda.app.Invoice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pialeda.app.Invoice.dto.GlobalUser;
import pialeda.app.Invoice.model.User;
import pialeda.app.Invoice.service.UserService;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    //    ADMIN
//    @GetMapping("admin-dashboard")
//    public String dashboard(){
//        return "admin/dashboard";
//    }

    @GetMapping("admin-users")
    public String users(Model model){
        String role = GlobalUser.getUserRole();
        String userFname = GlobalUser.getUserFirstName();
        String userLname = GlobalUser.getUserLastName();
        String fullName = userLname+", "+userFname;
        String destination=null;
        if(role == null){
            return destination = "redirect:/login";
        } else if (role.equals("vr-staff")) {
            return destination = "redirect:/vr/user/invoices";
        } else if (role.equals(("marketing"))) {
            return destination = "redirect:marketing-invoice";
        } else if (role.equals("admin")) {
            model.addAttribute("userList", userService.getAllUser());
            model.addAttribute("user", new User());
            model.addAttribute("fullName",fullName);
            return destination = "admin/users";
        }
        return destination;
    }

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute("user") User user) {
        userService.createUser(user);
        return "redirect:/admin-users";
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam(value = "uPassword", required = false) String uPassword, @ModelAttribute("user") User user) {
        userService.updateUser(user, uPassword);
        return "redirect:/admin-users";
    }

    @PostMapping("/deleteUser")
    public String deleteItem(@ModelAttribute("user") User user) {
        userService.deleteUser(user.getId());
        return "redirect:/admin-users";
    }
}
