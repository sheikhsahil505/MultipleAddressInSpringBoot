package com.springboot.controller;

import com.springboot.model.Address;
import com.springboot.model.User;
import com.springboot.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private Model model;
    @Mock
    private HttpSession session;
    @Mock
    private Address address;
    @Mock
    private User user;
    @Mock
    User mockUser = new User();
    @Mock
    List<User> mockUserList = new ArrayList<>();
    @Mock
    List<Address> mockAddressList = new ArrayList<>();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testViewPage() {
        String result = userController.viewPage();
        assertEquals("index", result);
    }

    @Test
    public void testLoginPage() {
        String result = userController.loginPage();
        assertEquals("index", result);
    }

    @Test
    public void testSignUp() {
        String result = userController.signUp();
        assertEquals("newRegister", result);
    }

    @Test
    public void testForgetPassword() {
        String result = userController.forgetPassword();
        assertEquals("forgetPassword", result);
    }

    @Test
    public void testSaveUserWithValidationErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = userController.saveUser(user, bindingResult, address, model);

        verify(model).addAttribute(eq("ErrorFromBackend"), any());
        assertEquals("newRegister", viewName);
    }

    @Test
    public void testSaveUserEmailTaken() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.saveUser(any(User.class), any(Address.class))).thenReturn(true);

        String viewName = userController.saveUser(user, bindingResult, address, model);

        verify(model).addAttribute("errorMessage", "email is already taken");
        assertEquals("newRegister", viewName);
    }

    @Test
    public void testSaveUserSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.saveUser(any(User.class), any(Address.class))).thenReturn(false);

        String viewName = userController.saveUser(user, bindingResult, address, model);

        verify(model).addAttribute("errorMessage", "Register Successfully");
        assertEquals("newRegister", viewName);
    }

    @Test
    public void testLoginSuccess() {
        when(userService.login(any(User.class))).thenReturn(user);
        when(userService.findAll()).thenReturn(mockUserList);
        when(userService.findAllAddressByUser(user)).thenReturn(mockAddressList);
        String viewName = userController.login(user, model);
        verify(model).addAttribute("addresses", mockAddressList);
        verify(model).addAttribute("registrations", mockUserList);
        verify(model).addAttribute("profile", user);
        assertEquals("view", viewName);
    }

    @Test
    public void testLoginFailure() {
        when(userService.login(any(User.class))).thenReturn(null);
        String viewName = userController.login(user, model);
        verify(model).addAttribute("errorMessage", "Invalid username and password");
        assertEquals("index", viewName);
    }
    @Test
    public void testViewMoreUserSuccess() {
        int userId = 1;
        when(session.getAttribute("username")).thenReturn("testUsername");
        when(userService.getUserById(userId)).thenReturn(user);
        when(userService.findAll()).thenReturn(mockUserList);
        when(userService.findAllAddressByUser(user)).thenReturn(mockAddressList);
        String viewName = userController.viewMoreUser(userId, model, session);
        verify(session).setAttribute("userEmail", user.getEmail());
        verify(model).addAttribute("addresses", mockAddressList);
        verify(model).addAttribute("registrations", mockUserList);
        verify(model).addAttribute("profile", user);
        assertEquals("view", viewName);
    }
    @Test
    public void testSessionIsNull() {
        int userId = 1;
        when(session.getAttribute("username")).thenReturn(null);
        String viewName = userController.viewMoreUser(userId, model, session);
        assertEquals("index", viewName);
    }

    @Test
    public void testUpdateUser() {
        long userId = 1;
        when(session.getAttribute("username")).thenReturn("testUsername");
        when(userService.getUserById(userId)).thenReturn(mockUser);
        when(userService.findAllAddressByUser(mockUser)).thenReturn(mockAddressList);
        String viewName = userController.updateUser(userId, model);
        verify(model).addAttribute("profile", mockUser);
        verify(model).addAttribute("addresses", mockAddressList);
        assertEquals("updateProfile", viewName);
    }

    @Test
    public void testUpdateProfileInvalidSession() {
        when(session.getAttribute("username")).thenReturn(null);
        String viewName = userController.updateProfile("addressDeleteIds", user, address,model);
        assertEquals("index", viewName);
        verify(session, times(1)).getAttribute("username");
    }


    @Test
  public void home() {

        when(session.getAttribute("username")).thenReturn("testUsername");
        when(userService.home()).thenReturn(user);
        when(userService.findAll()).thenReturn(mockUserList);
        when(userService.findAllAddressByUser(user)).thenReturn(mockAddressList);

        // Call the controller method
        String viewName = userController.home(model);

        // Verify that the expected methods were called
        verify(userService, times(1)).home();
        verify(userService, times(1)).findAll();
        verify(userService, times(1)).findAllAddressByUser(user);

        // Check that the view name matches the expected value
        assertEquals("view", viewName);

        // Verify that attributes were added to the model
        verify(model).addAttribute("profile", user);
        verify(model).addAttribute("addresses", mockAddressList);
        verify(model).addAttribute("registrations", mockUserList);

    }

    @Test
   public void deleteUSer() {

        long userId = 75L;
        when(session.getAttribute("username")).thenReturn("testUsername");
        when(userService.home()).thenReturn(user);
        when(userService.findAll()).thenReturn(mockUserList); // Replace with a list of users
        when(userService.findAllAddressByUser(user)).thenReturn(mockAddressList); // Replace with a list of addresses

        // Call the controller method
        String viewName = userController.deleteUSer(userId);

        // Verify that the userService methods were called
        verify(userService).deleteUser(userId);
        verify(userService).home();
        verify(userService).findAll();
        verify(userService).findAllAddressByUser(user);

        // Verify that the model attributes were added
        verify(model).addAttribute("addresses", mockAddressList);
        verify(model).addAttribute("registrations",mockUserList);
        verify(model).addAttribute("profile", user);

        // Check the expected view name
        assertEquals("view", viewName);
    }



    @Test
    public void testUpdateProfileEmailTaken() {
//        // Arrange
//        User user = new User(); // Create a User object
//        Address address = new Address(); // Create an Address object
        when(session.getAttribute("username")).thenReturn("testUsername");
        when(userService.updateUser(user, address)).thenReturn(true);

        // Act
        String result = userController.updateProfile("1,2,3", user, address,model);

        assertEquals("updateProfile", result);
        verify(model).addAttribute(eq("errorMessage"), eq("Email is already taken."));


    }

    @Test
    public void testUpdateProfileEmailNotTaken() {

        when(session.getAttribute("username")).thenReturn("testUsername");
        when(userService.updateUser(user, address)).thenReturn(false);


        String result = userController.updateProfile("1,2,3", user, address,model);
        assertEquals("view", result);


    }




    @Test
    public void logout() {
        doNothing().when(userService).logout();
        String result = userController.logout();
        assertEquals("index", result); // You can adjust the expected result
        verify(userService, times(1)).logout();
    }
}