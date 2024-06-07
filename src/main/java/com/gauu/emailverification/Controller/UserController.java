package com.gauu.emailverification.Controller;

import com.gauu.emailverification.user.IUserService;
import com.gauu.emailverification.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/users")
public class UserController {
    private final IUserService userService;
     public UserController(UserService userService) {
         this.userService = userService;
     }

     @GetMapping
     public String getUser(Model model){
         model.addAttribute("users",userService.getAllUsers());
         return "users";
     }
}
