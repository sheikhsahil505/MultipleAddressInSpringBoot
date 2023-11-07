package com.springboot.service;

import com.springboot.model.Address;
import com.springboot.model.User;
import com.springboot.repository.AddressRepository;
import com.springboot.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpSession session;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    Address address = new Address();
    @Mock
    User user = new User();
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveUser() {
        user.setEmail("test@example.com");

        Address address = new Address();
        address.setStreet("Street1,Street2");
        address.setApartment("Apt1,Apt2");
        address.setCity("City1,City2");
        address.setState("State1,State2");
        address.setCountry("Country1,Country2");
        address.setPincode("Pincode1,Pincode2");

        // Mocking UserRepository behavior
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        // Mocking AddressRepository behavior
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        boolean result = userService.saveUser(user, address);

        // Assertions
        assertFalse(result);
    }

    @Test
    public void testLogin() {

        String email = "test@example.com";
        String password = "password";
         user.setUser_id(1L);
        user.setRole("user");
        when(userRepository.findByEmailAndPassword(email, password)).thenReturn(user);
        when(userRepository.findByEmailAndPassword(email,password)).thenReturn(user);
        User result = userService.login(user);
        assertNotEquals(user, result);
    }

    @Test
    public void testFindAll() {
        List<User> users = new ArrayList<>();

        // Mocking UserRepository behavior
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        // Assertions
        assertEquals(users, result);
    }

    @Test
    public void testFindAllAddressByUser() {
        user.setUser_id(1L); // Replace with a valid user ID
        List<Address> addresses = new ArrayList<>();

        // Mocking AddressRepository behavior
        when(addressRepository.findAllByUserId(user.getUser_id())).thenReturn(addresses);

        List<Address> result = userService.findAllAddressByUser(user);

        // Assertions
        assertEquals(addresses, result);
    }

    @Test
    public void testGetUserById() {
        user.setUser_id(1L); // Replace with a valid user ID

        // Mocking UserRepository behavior
        when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));

        User result = userService.getUserById(user.getUser_id());

        // Assertions
        assertEquals(user, result);
    }

    @Test
    public void testUpdateUser_SuccessfulUpdate() {
        User user = new User();
        user.setUser_id(1L);
        user.setEmail("new-email@example.com");

        Address address = new Address();
        address.setAddressId(new long[] { 1L, 2L });
        address.setStreet("NewStreet1,NewStreet2");
        address.setApartment("NewApt1,NewApt2");
        address.setCity("NewCity1,NewCity2");
        address.setState("NewState1,NewState2");
        address.setCountry("NewCountry1,NewCountry2");
        address.setPincode("NewPincode1,NewPincode2");

        // Mocking session
        when(session.getAttribute("userEmail")).thenReturn("new-email@example.com");

        // Mocking UserRepository behavior
        when(userRepository.findByEmail("new-email@example.com")).thenReturn(null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Mocking AddressRepository behavior
        when(addressRepository.findById(1L)).thenReturn(Optional.of(new Address()));
        when(addressRepository.findById(2L)).thenReturn(Optional.of(new Address()));

        boolean result = userService.updateUser(user, address);

        // Assertions
        assertFalse(result);
    }
    @Test
    public void testDeleteAddress() {
        String deleteAd = "1,2,3"; // Replace with valid address IDs

        userService.deleteAddress(deleteAd);

        // Mocking AddressRepository behavior
        verify(addressRepository, times(3)).deleteById(anyLong());
    }

    @Test
    public void testHome() {
        String username = "test@example.com";
        String password = "password";

        // Mocking HttpSession behavior
        when(session.getAttribute("username")).thenReturn(username);
        when(session.getAttribute("password")).thenReturn(password);

        // Mocking UserRepository behavior
        when(userRepository.findByEmailAndPassword(username, password)).thenReturn(user);

        User result = userService.home();

        // Assertions
        assertEquals(user, result);
    }

    @Test
    public void testDeleteUser() {
        long userId = 1L; // Replace with a valid user ID

        userService.deleteUser(userId);

        // Mocking UserRepository behavior
        verify(userRepository).deleteById(userId);
    }

    @Test
    public void testLogout() {
        userService.logout();

        // Mocking HttpSession behavior
        verify(session).removeAttribute("username");
        verify(session).invalidate();
    }
}
