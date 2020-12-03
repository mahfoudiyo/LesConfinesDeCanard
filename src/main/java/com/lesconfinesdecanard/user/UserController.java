package com.lesconfinesdecanard.user;


import com.lesconfinesdecanard.like.LikesRepository;
import com.lesconfinesdecanard.recipe.RecipeRepository;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final LikesRepository likesRepository;


    public UserController (UserRepository userRepository, RecipeRepository recipeRepository, LikesRepository likesRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.likesRepository = likesRepository;
    }

    @GetMapping ("/connexion")
    public String GetConnexion (Model model,HttpSession session) {
        session.removeAttribute("user");
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
        return "redirect:/app";
    }

    @GetMapping ("/profil")
    public String GetProfil (Model model, HttpSession session, ModelMap map) {
        if(session.getAttribute("user") == null){
            return("redirect:connexion");
        }
        model.addAttribute ("user", session.getAttribute("user"));
        User currentUser = (User) session.getAttribute("user");
        map.addAttribute("myRecipies", recipeRepository.findAllByUserId(currentUser.getId()));
        map.addAttribute("recipiesLiked", likesRepository.findByUser_id(currentUser.getId()));
        return "profil.html";
    }




}
