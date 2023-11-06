package com.springboot.controller;

import com.springboot.service.ForgetPasswordService;
import com.springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.ui.Model;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ForgetPasswordControllerTest {
    ForgetPasswordService forgetPasswordService = mock(ForgetPasswordService.class);
    Model model = mock(Model.class);

    @Test
    public void testSentOTP_ValidEmailAndDOB_ReturnsVerifyOTP() {
        ForgetPasswordController forgetPasswordController = new ForgetPasswordController(forgetPasswordService,model);
        String email = "sahilsarkarphotography@gmail.com";
        String dob = "10-01-2001";
        when(forgetPasswordService.verifyEmailandDob(email, dob)).thenReturn(true);
        String result = forgetPasswordController.sentOTP(email, dob);
        assertEquals("verifyOTP", result);
        verify(model, never()).addAttribute(eq("errorMessage"), any());
    }
    @Test
    public void testSentOTP_InvalidEmailAndDOB_ReturnsErrorMessage() {
        ForgetPasswordController forgetPasswordController = new ForgetPasswordController(forgetPasswordService, model); // Pass both the model and service
        String email = "invalidemail@example.com";
        String dob = "01-01-2000";
        when(forgetPasswordService.verifyEmailandDob(email, dob)).thenReturn(false);
        String result = forgetPasswordController.sentOTP(email, dob);
        assertEquals("forgetPassword", result);
        verify(model).addAttribute("errorMessage", "Invalid email and DOB");
    }

    @Test
    public void testVerifyOtp_ValidOtp_ReturnsChangePassword() {
        ForgetPasswordController forgetPasswordController = new ForgetPasswordController(forgetPasswordService, model);
        String otp = "123456";
        when(forgetPasswordService.verifyOtp(otp)).thenReturn(true);
        String result = forgetPasswordController.verifyOtp(otp);
        assertEquals("changePassword", result);
        verify(model, never()).addAttribute(eq("errorMessage"), any());
    }

    @Test
    public void testVerifyOtp_InvalidOtp_ReturnsErrorMessage() {
        ForgetPasswordController forgetPasswordController = new ForgetPasswordController(forgetPasswordService, model);
        String otp = "invalid_otp"; // Replace with an invalid OTP value
        when(forgetPasswordService.verifyOtp(otp)).thenReturn(false);
        String result = forgetPasswordController.verifyOtp(otp);
        assertEquals("verifyOTP", result);
        verify(model).addAttribute("errorMessage", "Invalid Your OTP");
    }
    @Test
    public void testGenerateNewPassword() {
        ForgetPasswordController forgetPasswordController = new ForgetPasswordController(forgetPasswordService, model);
        String newPassword = "new_password";
        String result = forgetPasswordController.generateNewPassword(newPassword);

        verify(forgetPasswordService).getNewPassword(newPassword);
        assertEquals("index", result);
    }

}
