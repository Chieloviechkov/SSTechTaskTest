package com.example.sstechtask.controllers;

import com.example.sstechtask.model.UserEntity;
import com.example.sstechtask.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void createUser_ValidUser_ReturnsUser() throws Exception {
        UserEntity user = new UserEntity(null, "user@example.com", "John", "Doe", new Date(), null, null);
        Mockito.when(userService.createUser(any(UserEntity.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    public void updateUser_ExistingUser_ReturnsUpdatedUser() throws Exception {
        UserEntity updatedUser = new UserEntity(1L, "updated@example.com", "John", "Doe", new Date(), "New Address", "1234567890");
        Mockito.when(userService.updateUser(Mockito.eq(1L), any(UserEntity.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    public void deleteUser_ExistingUser_ReturnsOk() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void findByBirthDateRange_ValidRange_ReturnsUsers() throws Exception {
        UserEntity user1 = new UserEntity(1L, "user1@example.com", "Alice", "Smith", new Date(), null, null);
        UserEntity user2 = new UserEntity(2L, "user2@example.com", "Bob", "Brown", new Date(), null, null);
        Mockito.when(userService.findUsersByBirthDateRange(any(Date.class), any(Date.class))).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users/search")
                        .param("from", "2020-01-01")
                        .param("to", "2020-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email").value("user1@example.com"));
    }


}
