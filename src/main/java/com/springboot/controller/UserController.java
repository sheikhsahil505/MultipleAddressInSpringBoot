package com.springboot.controller;

import com.springboot.model.Address;
import com.springboot.model.User;
import com.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession session;
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
    @PostMapping(value = "/login")
    public String login(User user,Model model){
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
        User user = userService.getUserById(id);
        session.setAttribute("userEmail",user.getEmail());
        List<User> all = userService.findAll();
        List<Address> allAddressByUser = userService.findAllAddressByUser(user);
        model.addAttribute("addresses",allAddressByUser);
        model.addAttribute("registrations",all);
        model.addAttribute("profile",user);
            return "view";
    }

    @PostMapping(value = "/UpdateProfile")
    public String updateUser(@RequestParam("user_id") long id, Model model) {
        User user = userService.getUserById(id);
        List<Address> allAddressByUserId = userService.findAllAddressByUser(user);
        model.addAttribute("profile", user);
        model.addAttribute("addresses", allAddressByUserId);
        return "updateProfile";
    }

    @RequestMapping(value = "/home",method = RequestMethod.GET)
    public String home( HttpSession session,Model model) {
        User home = userService.home();
        if(home!=null){
            List<User> all = userService.findAll();
            List<Address> allAddressByUser = userService.findAllAddressByUser(home);
            model.addAttribute("addresses",allAddressByUser);
            model.addAttribute("registrations",all);
            model.addAttribute("profile",home);
            return "view";
            }
        return null;
    }

    @PostMapping(value = "/saveUpdateUser")
    public String updateProfile(@RequestParam("removedAddressIds") String addressDeleteIds, User user,Address address, HttpSession session, Model model) {
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
}

    @PostMapping(value = "/deleteUser")
    public String deleteUSer(@RequestParam("user_id") long id, Model model){
        userService.deleteUser(id);
        User home = userService.home();
        if(home!=null){
            List<User> all = userService.findAll();
            List<Address> allAddressByUser = userService.findAllAddressByUser(home);
            model.addAttribute("addresses",allAddressByUser);
            model.addAttribute("registrations",all);
            model.addAttribute("profile",home);
            return "view";
        }
        return null;
    }
}
