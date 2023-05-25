package com.climpoche.user.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("User")
public class UserDTO {

    @Id
    private String id;
    private String name;
    private String roleName;
    private String status;

}
