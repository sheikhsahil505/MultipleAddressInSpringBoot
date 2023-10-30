package com.springboot.repository;

import com.springboot.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long>{
    @Query("SELECT a FROM Address a WHERE a.user.user_id = :userId")
    List<Address> findAllByUserId(Long userId);
}
