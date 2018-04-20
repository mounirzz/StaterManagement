package com.micro.demo.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.micro.demo.models.User;

public interface UserRepository extends CrudRepository<User, Long> {
User findOneByUserName(String name);
User findOneByEmail(String Email);
User findOneByUserNameOrEmail(String username, String email);
User findOneByToken(String token);

@Modifyong
@Transactional
}
