package com.springboot.service;

import com.springboot.model.User;
import com.springboot.repository.UserRepository;
import com.springboot.util.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Random;
@Service
public class ForgetPasswordServiceImpl implements ForgetPasswordService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private HttpSession session;
    Random random= new Random(1000);
    @Override
    public boolean verifyEmailandDob(String email, String dob) {
        User byEmail = userRepository.findByEmail(email);
        boolean flag=false;
        if (byEmail!=null) {
            int otp = random.nextInt(9999999);
            String subject = "OTP from Spring MVC task";
            session.setAttribute("email", email);
            session.setAttribute("dob", dob);
            String message = " Your  OTP is " + otp;
            session.setAttribute("otp", otp);
        boolean f = emailService.sendEmail(subject, message, email);
            flag=f;
        }

        return flag;
    }

    @Override
    public void getNewPassword(String newPassword) {
        String email =(String)   session.getAttribute("email");
//        String dob =(String) session.getAttribute("dob");
//        String encoder = Base64.getEncoder().encodeToString(newPassword.getBytes(Charset.forName("UTF-8")));
        User byEmail = userRepository.findByEmail(email);
        byEmail.setPassword(newPassword);
        userRepository.save(byEmail);
        session.invalidate();
    }

    @Override
    public boolean verifyOtp(String otp) {
        int otpValue = Integer.parseInt(otp);
        int otp1 =(int) session.getAttribute("otp");
        if(otpValue==otp1){
            return true;
        }else {
            return false;
        }
    }

}