package com.example.sstechtask.service;

import com.example.sstechtask.model.UserEntity;
import com.example.sstechtask.repo.UserRepository;
import com.example.sstechtask.utils.AgeRestrictionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    public void testCreateUserValidAge() {
        Date validBirthDate = getValidBirthDate(25);
        UserEntity user = new UserEntity();
        user.setBirthDate(validBirthDate);

        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity createdUser = userService.createUser(user);
        assertNotNull(createdUser);
        assertEquals(validBirthDate, createdUser.getBirthDate());
    }

    @Test
    public void testCreateUserInvalidAge() {
        Date invalidBirthDate = getValidBirthDate(15);
        UserEntity user = new UserEntity();
        user.setBirthDate(invalidBirthDate);

        when(userRepository.save(any(UserEntity.class))).thenThrow(AgeRestrictionException.class);

        assertThrows(AgeRestrictionException.class, () -> {
            userService.createUser(user);
        });
    }


    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        UserEntity existingUser = new UserEntity();
        existingUser.setId(userId);
        existingUser.setFirstName("John");

        UserEntity updates = new UserEntity();
        updates.setFirstName("Jane");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(existingUser);

        UserEntity updatedUser = userService.updateUser(userId, updates);
        assertNotNull(updatedUser);
        assertEquals("Jane", updatedUser.getFirstName());
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;

        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testFindUsersByBirthDateRange() {
        Date from = getValidBirthDate(30);
        Date to = getValidBirthDate(20);

        userService.findUsersByBirthDateRange(from, to);

        verify(userRepository, times(1)).findAllByBirthDateBetween(from, to);
    }

    @Test
    public void testFindUsersByBirthDateRangeInvalid() {
        Date from = getValidBirthDate(20);
        Date to = getValidBirthDate(30);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.findUsersByBirthDateRange(from, to);
        });
    }

    private Date getValidBirthDate(int years) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -years);
        return cal.getTime();
    }
}
