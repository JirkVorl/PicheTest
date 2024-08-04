package org.example.pichetest.controller;

import org.example.pichetest.dto.request.UserRequestDto;
import org.example.pichetest.dto.response.UserResponseDto;
import org.example.pichetest.model.User;
import org.example.pichetest.service.AuthenticationService;
import org.example.pichetest.service.mapper.ResponseDtoMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final ResponseDtoMapper<UserResponseDto, User> userResponseMapper;

    public AuthenticationController(AuthenticationService authenticationService,
                                    ResponseDtoMapper<UserResponseDto, User> userResponseMapper) {
        this.authenticationService = authenticationService;
        this.userResponseMapper = userResponseMapper;
    }

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody UserRequestDto requestDto) {
        return userResponseMapper.mapToDto(
            authenticationService.register(requestDto.getEmail(), requestDto.getPassword()));
    }
}
