package org.example.pichetest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.example.pichetest.dto.request.UserRequestDto;
import org.example.pichetest.dto.response.UserResponseDto;
import org.example.pichetest.model.User;
import org.example.pichetest.service.AuthenticationService;
import org.example.pichetest.service.mapper.ResponseDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private ResponseDtoMapper<UserResponseDto, User> userResponseMapper;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    void testRegister() throws Exception {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setEmail("test@example.com");

        when(authenticationService.register(any(String.class), any(String.class))).thenReturn(user);
        when(userResponseMapper.mapToDto(any(User.class))).thenReturn(responseDto);

        mockMvc.perform(post("/register")
                .with(csrf())
                .contentType("application/json")
                .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.email").value("test@example.com"));
    }
}