package com.climpoche.user.service;

import com.climpoche.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    MongoTemplate mongoTemplate;

    public UserDTO createUserDTO(UserDTO userDTO){
        mongoTemplate.save(userDTO);
        return userDTO;
    }
}
