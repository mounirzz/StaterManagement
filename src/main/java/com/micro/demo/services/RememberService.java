package com.micro.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro.demo.models.Remember;
import com.micro.demo.repositories.RememberRepository;

@Service
public class RememberService {

    @Autowired
    RememberRepository rememberRepository;

    public void add(Remember remember) {
        rememberRepository.save(remember);
    }

    public void delete(String uuid){
        rememberRepository.delete(uuid);
    }

    public Remember findById(String uuid){
        return rememberRepository.findOne(uuid);
    }
}
