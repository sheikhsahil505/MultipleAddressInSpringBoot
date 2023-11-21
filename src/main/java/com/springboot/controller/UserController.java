package com.springboot.controller;

import com.springboot.model.Address;
import com.springboot.model.User;
import com.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
@EnableOAuth2Sso
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession session;
    private   Model model;
    @GetMapping("/view")
    public String viewPage(){return "index";}
    @GetMapping("/loginBtn")
    public String loginPage(){return "index";}
    @GetMapping("/signUp")
    public String signUp(){
        return "newRegister";
    }
    @GetMapping("/forgetPassword")
    public String forgetPassword(){
        return "forgetPassword";
    }

    @PostMapping(value = "/SaveUser")
    public String saveUser(@Valid User user, BindingResult br, Address address, Model model){
        if (br.hasErrors()) {
            model.addAttribute("ErrorFromBackend", br.getAllErrors());
        }else{
            boolean status = userService.saveUser(user, address);
            if(status){
                model.addAttribute("errorMessage", "email is already taken");
                return "newRegister" ;
            }
            model.addAttribute("errorMessage", "Register Successfully");
        }
                return "newRegister";
    }
    @GetMapping("/")
    public String login(OAuth2Authentication authentication,Model model) {
        LinkedHashMap<String, Object> details = (LinkedHashMap<String, Object>) authentication.getUserAuthentication().getDetails();
        String email = (String)details.get("email");
        User user = new User();
        user.setEmail(email);
        User login = userService.login(user);
        if(login!=null){
            List<User> all = userService.findAll();
            List<Address> allAddressByUser = userService.findAllAddressByUser(login);
            model.addAttribute("addresses",allAddressByUser);
            model.addAttribute("registrations",all);
            model.addAttribute("profile",login);
            return "view";
        }else{
            model.addAttribute("errorMessage","Invalid username and password");
            return "index";
        }
    }
    @PostMapping(value = "/viewUser")
    public String viewMoreUser(@RequestParam("user_id") int id, Model model, HttpSession session) {
        if(session.getAttribute("username")!=null) {
            User user = userService.getUserById(id);
            session.setAttribute("userEmail", user.getEmail());
            List<User> all = userService.findAll();
            List<Address> allAddressByUser = userService.findAllAddressByUser(user);
            model.addAttribute("addresses", allAddressByUser);
            model.addAttribute("registrations", all);
            model.addAttribute("profile", user);
            return "view";
        }else{
            return "index";
        }
    }

    @PostMapping(value = "/UpdateProfile")
    public String updateUser(@RequestParam("user_id") long id, Model model) {
        if(session.getAttribute("username")!=null) {
            User user = userService.getUserById(id);
            List<Address> allAddressByUserId = userService.findAllAddressByUser(user);
            model.addAttribute("profile", user);
            model.addAttribute("addresses", allAddressByUserId);
            return "updateProfile";
        }else {
            return "index";
        }
    }

    @GetMapping(value = "/home")
    public String home( Model model) {
        if(session.getAttribute("username")!=null) {
            User home = userService.home();
                List<User> all = userService.findAll();
                List<Address> allAddressByUser = userService.findAllAddressByUser(home);
                model.addAttribute("addresses", allAddressByUser);
                model.addAttribute("registrations", all);
                model.addAttribute("profile", home);

            return "view";
        }else {
            return "index";
        }
    }

    @PostMapping(value = "/saveUpdateUser")
    public String updateProfile(@RequestParam("removedAddressIds") String addressDeleteIds, User user,Address address,Model model) {
        if(session.getAttribute("username")!=null){
            long id = user.getUser_id();
            boolean status = userService.updateUser(user, address);
            if (status) {
                User userById = userService.getUserById(id);
                List<Address> allAddressByUserId = userService.findAllAddressByUser(userById);
                model.addAttribute("profile", userById);
                model.addAttribute("addresses", allAddressByUserId);
                model.addAttribute("errorMessage", "Email is already taken.");
                return "updateProfile";
            } else {
                userService.deleteAddress(addressDeleteIds);
                User userById = userService.getUserById(id);
                List<Address> allAddressByUserId = userService.findAllAddressByUser(userById);
                List<User> allUsers = userService.findAll();
                model.addAttribute("profile", userById);
                model.addAttribute("addresses", allAddressByUserId);
                model.addAttribute("registrations", allUsers);
                return "view";
            }
            } else{
            return "index";
            }
}
    @PostMapping(value = "/deleteUser")
    public String deleteUSer(@RequestParam("user_id") long id,Model model){
        if(session.getAttribute("username")!=null){
            userService.deleteUser(id);
            User home = userService.home();
            List<User> all = userService.findAll();
            List<Address> allAddressByUser = userService.findAllAddressByUser(home);
            model.addAttribute("addresses",allAddressByUser);
            model.addAttribute("registrations",all);
            model.addAttribute("profile",home);
            return "view";
        } else {
            return "index";
        }
    }
    @GetMapping(value = "/logout")
    public String logout(){
        userService.logout();
        return "index";
    }
}
