package com.micro.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micro.demo.models.Remember;

public interface RememberRepository extends JpaRepository<Remember, String> {

}
