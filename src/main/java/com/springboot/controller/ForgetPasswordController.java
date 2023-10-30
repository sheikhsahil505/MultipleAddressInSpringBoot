package com.springboot.controller;



import com.springboot.service.ForgetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;


@Controller
public class ForgetPasswordController {
    @Autowired
    private ForgetPasswordService forgetPassword;

    @RequestMapping("/forget")
    public String openForgetPage(){
        return "forgetPassword";
    }


@RequestMapping(value = "/sendOtp",method = RequestMethod.POST)
public String sentOTP(@RequestParam("email") String email, @RequestParam("dob") String dob, Model model, HttpSession session) {
    boolean flag = forgetPassword.verifyEmailandDob(email, dob);

    if (flag) {
        return "verifyOTP";

    } else {
        model.addAttribute("errorMessage", "Invalid email and DOB");
        return "forgetPassword";
    }
}
@RequestMapping(value = "/getOTP",method = RequestMethod.POST)
    public String verifyOtp(@RequestParam("otp") String otp,HttpSession session,Model model){
    int otpValue = Integer.parseInt(otp);
    int otp1 =(int) session.getAttribute("otp");
    if(otpValue==otp1){
        return "changePassword";
    }
    model.addAttribute("errorMessage", "Invalid Your OTP");
    return "verifyOTP";

}
    @RequestMapping(value = "/generateNewPassword",method = RequestMethod.POST)
    public String generateNewPassword(@RequestParam("password")String newPassword, HttpSession session, Model model){
        forgetPassword.getNewPassword(newPassword);
         model.addAttribute("errorMessage", "Your Password has SuccessFully changed");
         session.invalidate();
        return "changePassword";
    }
}