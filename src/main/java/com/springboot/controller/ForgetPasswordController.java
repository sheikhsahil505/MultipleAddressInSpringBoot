package com.springboot.controller;



import com.springboot.service.ForgetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class ForgetPasswordController {
    private ForgetPasswordService forgetPassword;
    private Model model;
    @Autowired
    public ForgetPasswordController(ForgetPasswordService forgetPassword) {
        this.forgetPassword = forgetPassword;
    }

    public ForgetPasswordController(ForgetPasswordService forgetPassword, Model model) {
        this.forgetPassword = forgetPassword;
        this.model = model;
    }

    @GetMapping("/forget")
    public String openForgetPage(){
        return "forgetPassword";
    }


@PostMapping(value = "/sendOtp")
public String sentOTP(@RequestParam("email") String email, @RequestParam("dob") String dob) {
    boolean flag = forgetPassword.verifyEmailandDob(email, dob);
    if (flag) {
        return "verifyOTP";
    } else {
        model.addAttribute("errorMessage", "Invalid email and DOB");
        return "forgetPassword";
    }
}
@PostMapping(value = "/getOTP")
    public String verifyOtp(@RequestParam("otp") String otp){
    boolean status = forgetPassword.verifyOtp(otp);
    if(status){
        return "changePassword";
    }else {
        model.addAttribute("errorMessage", "Invalid Your OTP");
        return "verifyOTP";
    }
}
    @PostMapping(value = "/generateNewPassword")
    public String generateNewPassword(@RequestParam("password")String newPassword){
         forgetPassword.getNewPassword(newPassword);
//         model.addAttribute("errorMessage", "Your Password has SuccessFully changed");
        return "index";
    }
}