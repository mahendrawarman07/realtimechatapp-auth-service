package com.example.authservice.dtos;

import com.example.authservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserDto {
    private UUID id;
    private String name;
    private String email;

    public static UserDto from(User user){
        if(user == null)
            return null;

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

}
