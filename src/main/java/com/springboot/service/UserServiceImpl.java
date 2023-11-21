package com.springboot.service;

import com.springboot.model.Address;
import com.springboot.model.User;
import com.springboot.repository.AddressRepository;
import com.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpSession session;
    @Autowired
    private AddressRepository addressRepository;
        @Override
        public boolean saveUser(User user, Address address) {
            String email = user.getEmail();
            User byEmail = userRepository.findByEmail(email);
            if(byEmail==null){
            userRepository.save(user);
            String[] street = address.getStreet().split(",");
            String[] apartment = address.getApartment().split(",");
            String[] city = address.getCity().split(",");
            String[] state = address.getState().split(",");
            String[] country = address.getCountry().split(",");
            String[] pincode = address.getPincode().split(",");
            if (street != null && street.length > 0) {
                for (int i = 0; i < street.length; i++) {
                    Address address1 = new Address();
                    address1.setStreet(street[i]);
                    address1.setApartment(apartment[i]);
                    address1.setPincode(pincode[i]);
                    address1.setState(state[i]);
                    address1.setCity(city[i]);
                    address1.setCountry(country[i]);
                    address1.setUser(user);
                    addressRepository.save(address1);
                }
            }
            return false;
            }else{
                return true;
            }
        }
    @Override
    public User login(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        User byEmailAndPassword = userRepository.findByEmail(email);
        if(byEmailAndPassword!=null){
            session.setAttribute("loginId",byEmailAndPassword.getUser_id());
            session.setAttribute("loginRole",byEmailAndPassword.getRole());
            session.setAttribute("username",email);
            session.setAttribute("password",password);
            session.setAttribute("userEmail",byEmailAndPassword.getEmail());
        }
        return byEmailAndPassword;
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public List<Address> findAllAddressByUser(User user) {
        long userId = user.getUser_id();
        return addressRepository.findAllByUserId(userId);
    }

    @Override
    public User getUserById(long id) {
        Optional<User> byId = userRepository.findById(id);
        User user = byId.orElse(null);
        return user;
    }

    @Override
    public boolean updateUser(User user, Address address) {
        Object oldEmail = session.getAttribute("userEmail");
        String email = user.getEmail();
        User byEmail = userRepository.findByEmail(email);
        if(oldEmail.equals(email) || byEmail==null){
        long userId = user.getUser_id();
        Optional<User> byId = userRepository.findById(userId);
        if(byId!=null){
            String password = byId.get().getPassword();
            user.setPassword(password);}
        userRepository.save(user);

        long[] addressId =address.getAddressId();
        String[] street = address.getStreet().split(",");
        String[] apartment = address.getApartment().split(",");
        String[] city = address.getCity().split(",");
        String[] state = address.getState().split(",");
        String[] country = address.getCountry().split(",");
        String[] pincode = address.getPincode().split(",");

        if (street != null && street.length > 0) {
            for (int i = 0; i < street.length; i++) {
                if (i < addressId.length) {
                    // Update the existing Address with provided address_id
                    Optional<Address> existingAddress = addressRepository.findById(addressId[i]);
                    if(addressId[i]!=0){
                        existingAddress.get().setAddress_id(addressId[i]);
                        existingAddress.get().setStreet(street[i]);
                        existingAddress.get().setApartment(apartment[i]);
                        existingAddress.get().setPincode(pincode[i]);
                        existingAddress.get().setState(state[i]);
                        existingAddress.get().setCity(city[i]);
                        existingAddress.get().setCountry(country[i]);
                        existingAddress.get().setUser(user);
                        Address address2 = existingAddress.orElse(null);
                        addressRepository.save(address2);
                    }
                } else {
                    // Create a new Address for a null address_id (generating it)
                    Address newAddress = new Address();
                    newAddress.setStreet(street[i]);
                    newAddress.setApartment(apartment[i]);
                    newAddress.setPincode(pincode[i]);
                    newAddress.setState(state[i]);
                    newAddress.setCity(city[i]);
                    newAddress.setCountry(country[i]);
                    newAddress.setUser(user);
                    addressRepository.save(newAddress);
                }
            }
        }
        return false;
        }
        return true;
    }
    @Override
    public void deleteAddress(String deleteAd) {
        String[] split = deleteAd.split(",");
        if (!"".equals(deleteAd)) {
            for (String idStr : split) {
                try {
                    Long id = Long.parseLong(idStr);
                    if (id > 0) {
                        addressRepository.deleteById(id);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public User home() {
        String username = (String) session.getAttribute("username");
        User byEmailAndPassword = userRepository.findByEmail(username);
        if(byEmailAndPassword!=null){
            session.setAttribute("userEmail",byEmailAndPassword.getEmail());
            return byEmailAndPassword;
        }else{
            return null;
        }
    }

    @Override
    public String deleteUser(long id) {
        userRepository.deleteById(id);
        return null;
    }


    @Override
    public void logout() {
        session.removeAttribute("username");
        session.invalidate();
    }
}
