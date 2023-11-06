package com.springboot.service;

public interface ForgetPasswordService {
    boolean verifyEmailandDob(String email, String dob);

    void getNewPassword(String newPassword);

    boolean verifyOtp(String otp);
}
