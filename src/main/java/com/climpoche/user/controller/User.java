package com.climpoche.user.controller;

import com.climpoche.user.dto.UserDTO;
import com.climpoche.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class User {

    @Autowired
    UserService userService;

    @GetMapping(value = "/user")
    public String get(){
        //save one user for everyget to check
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Sameesh");
        userDTO.setRoleName("support");
        userDTO.setStatus("ACTIVE");
        return userService.createUserDTO(userDTO).toString();
    }

}
