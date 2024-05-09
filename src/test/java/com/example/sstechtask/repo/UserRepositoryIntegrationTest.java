package com.example.sstechtask.repo;

import com.example.sstechtask.SsTechTaskApplication;
import com.example.sstechtask.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SsTechTaskApplication.class)
@ActiveProfiles("test")
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void userRepository_SavesAndRetrievesUser_Correctly() {
        UserEntity user = new UserEntity(null, "integration@test.com", "Integration", "Test", new Date(), null, null);
        user = userRepository.save(user);
        assertNotNull(user.getId());

        Optional<UserEntity> retrieved = userRepository.findById(user.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("integration@test.com", retrieved.get().getEmail());
    }
}
