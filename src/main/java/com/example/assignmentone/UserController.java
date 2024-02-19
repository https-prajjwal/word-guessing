package com.example.assignmentone;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    private UserService userService;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }





    @PostMapping("/save")
    public String saveWord(@ModelAttribute User user, RedirectAttributes redirectAttributes, @RequestParam String name, @RequestParam String password, @RequestParam String c_password, @RequestParam String email) {

        if (name.isEmpty()) {
            redirectAttributes.addFlashAttribute("successMessage", "No name provided!");
            return "redirect:/signup";
        } else if (email.isEmpty()) {
            redirectAttributes.addFlashAttribute("successMessage", "No email provided!");
            return "redirect:/signup";
        } else if (password.isEmpty()) {
            redirectAttributes.addFlashAttribute("successMessage", "No password provided!");
            return "redirect:/signup";
        } else if (c_password.isEmpty()) {
            redirectAttributes.addFlashAttribute("successMessage", "No confirm password provided!");
            return "redirect:/signup";
        }
         else if (!password.equals(c_password)) {
            redirectAttributes.addFlashAttribute("successMessage", "Passwords do not match");
            return "redirect:/signup";
        }
         else{
            userService.saveUser(user);

            redirectAttributes.addFlashAttribute("successMessage", "User successfully saved!");
            return "redirect:/signup";
        }
    }

    @GetMapping("/logout1")
    public String logout1(HttpSession session) {
        session.removeAttribute("user");
        session.removeAttribute("selectedLevel");
        return "redirect:/user";
    }
}

