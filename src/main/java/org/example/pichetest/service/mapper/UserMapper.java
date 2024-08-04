package org.example.pichetest.service.mapper;

import org.example.pichetest.dto.response.UserResponseDto;
import org.example.pichetest.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper implements ResponseDtoMapper<UserResponseDto, User> {

    @Override
    public UserResponseDto mapToDto(User user) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setEmail(user.getEmail());
        responseDto.setId(user.getId());
        return responseDto;
    }
}
