package com.lesconfinesdecanard.user;


import org.hibernate.HibernateException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Optional;

import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserRepository userRepository;

    public UserController (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping ("/connexion")
    public String GetConnexion (Model model) {
        model.addAttribute ("user", new User ());
        return "connexion.html";
    }

    @PostMapping("/register")
    public String PostRegister (@ModelAttribute User user, HttpSession session) {
        userRepository.save(user);
        session.setAttribute("user", user);
        return "connexion.html";
    }

    @PostMapping("/connexion")
    public String PostConnexion (@ModelAttribute User user, HttpSession session) {
        User newUser = userRepository.findByPseudoAndPassword(user.getPseudo(), user.getPassword());
        if (newUser == null) return "connexion.html";
        session.setAttribute("user", newUser);
        return "app.html";
    }

    @GetMapping ("/profil")
    public String GetProfil (Model model, HttpSession session) {
        model.addAttribute ("user", session.getAttribute("user"));
        return "profil.html";
    }

    @GetMapping("/modif")
    public String initUpdateUserForm(Model model, HttpSession session) {
        model.addAttribute ("user", session.getAttribute("user"));
        return "modif.html";
    }

    @PostMapping("/validFormModif")
    public String validUpdateUserForm(@ModelAttribute User user, HttpSession session) {
        User mYuser = (User) session.getAttribute("user");
        mYuser.setPassword(user.getPassword());
        mYuser.setPseudo(user.getPseudo());
        userRepository.save(mYuser);
        return "redirect:/profil";
    }


}
