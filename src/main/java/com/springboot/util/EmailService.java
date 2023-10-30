package com.springboot.util;

public interface EmailService {
     boolean sendEmail(String subject,String message,String to);

}
