package com.springboot.service;
import com.springboot.model.User;
import com.springboot.repository.UserRepository;
import com.springboot.util.EmailService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpSession;

@SpringBootTest
class ForgetPasswordServiceImplTest {
    @InjectMocks
     ForgetPasswordServiceImpl forgetPasswordService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;
    @Mock
    private HttpSession session;

    @Test
    public void testVerifyEmailandDob() {
        String email = "test@example.com";
        String dob = "1990-01-01";
        User mockUser = new User();
        Mockito.when(userRepository.findByEmail(email)).thenReturn(mockUser);
        String subject = "OTP from Spring MVC task";
        int otp = 123456;
        String message = " Your  OTP is " + otp;
        Mockito.when(emailService.sendEmail(subject, message, email)).thenReturn(true);
        boolean result = forgetPasswordService.verifyEmailandDob(email, dob);
        Mockito.verify(emailService, Mockito.times(0)).sendEmail(subject, message, email);
        assertFalse(result);
    }

    @Test
    public void testVerifyOtp() {
        int storedOtp = 123456; // Replace with the actual stored OTP value
        String validOtp = "123456";
        String invalidOtp = "654321";
        String nonIntegerOtp = "abc";

        // Mock the session to return the stored OTP
        Mockito.when(session.getAttribute("otp")).thenReturn(storedOtp);

        // Test with a valid OTP
        boolean resultValid = forgetPasswordService.verifyOtp(validOtp);
        assertTrue(resultValid);

        // Test with an invalid OTP (different value)
        boolean resultInvalid = forgetPasswordService.verifyOtp(invalidOtp);
        assertFalse(resultInvalid);

        // Test with an invalid OTP (non-integer format)
//        boolean resultInvalidFormat = forgetPasswordService.verifyOtp(nonIntegerOtp);
//        assertFalse(resultInvalidFormat);

        // Verify that the session.getAttribute method was called
        Mockito.verify(session, Mockito.times(2)).getAttribute("otp");
}

    @Test
    public void testGetNewPassword() {
        String email = "test@example.com";
        String newPassword = "newPassword";
        User mockUser = Mockito.mock(User.class);

        Mockito.when(session.getAttribute("email")).thenReturn(email);

        // Mock the userRepository to return the mockUser when findByEmail is called
        Mockito.when(userRepository.findByEmail(email)).thenReturn(mockUser);

        forgetPasswordService.getNewPassword(newPassword);

        // Verify that the session.getAttribute and userRepository methods were called
        Mockito.verify(session, Mockito.times(1)).getAttribute("email");
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);

        // Verify that the user's password was updated and saved
        Mockito.verify(mockUser, Mockito.times(1)).setPassword(newPassword); // Remove this line

        // Verify that userRepository's save method was called
        Mockito.verify(userRepository, Mockito.times(1)).save(mockUser);

        // Verify that the session was invalidated
        Mockito.verify(session, Mockito.times(1)).invalidate();
    }
}