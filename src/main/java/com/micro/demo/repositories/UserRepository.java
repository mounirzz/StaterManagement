package com.micro.demo.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.micro.demo.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findOneByUserName(String name);
    User findOneByEmail(String email);
    User findOneByUserNameOrEmail(String username, String email);
    User findOneByToken(String token);
    User findOneByUsernameandPassword(String password, String username);

    
    @Modifying
    @Transactional
    @Query("update User u set u.email = :email, u.firstname = :firstname, "
            + "u.lastname = :lastname, u.address = :address, u.companyName = :companyName "
            + "where u.userName = :userName")
    int updateUser(
            @Param("userName") String userName, 
            @Param("email") String email,
            @Param("firstname") String firstname,
            @Param("lastname") String lastname,
            @Param("address") String address,
            @Param("companyName") String companyName);
    @Modifying
    @Transactional
    @Query("update User u set u.lastLogin = CURRENT_TIMESTAMP where u.userName = ?1")
    int updateLastLogin(String userName);
    
    @Modifying
    @Transactional
    @Query("update User u set u.profilePicture = ?2 where u.userName = ?1")
    int updateProfilePicture(String userName, String profilePicture);
}