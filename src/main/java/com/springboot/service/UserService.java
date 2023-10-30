package com.springboot.service;

import com.springboot.model.Address;
import com.springboot.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    boolean saveUser(User user, Address address);

    User login(User user);

    List<User> findAll();

    List<Address> findAllAddressByUser(User user);

   User getUserById(long id);

    boolean updateUser(User user, Address address);

    void deleteAddress(String addressDeleteIds);

    User home();

    void deleteUser(long id);
}
