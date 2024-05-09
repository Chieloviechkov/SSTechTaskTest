package com.example.sstechtask.service;

import com.example.sstechtask.model.UserEntity;
import com.example.sstechtask.repo.UserRepository;
import com.example.sstechtask.utils.AgeRestrictionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Value("${minimum.age}")
    private int minimumAge;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity createUser(UserEntity user) {
        if (!isValidAge(user.getBirthDate())) {
            throw new AgeRestrictionException("User must be at least " + minimumAge + " years old.");
        }
        return userRepository.save(user);
    }

    @Transactional
    public UserEntity updateUser(Long id, UserEntity userUpdates) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        updateUserInfo(user, userUpdates);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<UserEntity> findUsersByBirthDateRange(Date from, Date to) {
        if (from.after(to)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }
        return userRepository.findAllByBirthDateBetween(from, to);
    }

    public boolean isValidAge(Date birthDate) {
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthDate);

        Calendar minAgeCalendar = Calendar.getInstance();
        minAgeCalendar.add(Calendar.YEAR, -minimumAge);

        return minAgeCalendar.after(birthCalendar);
    }


    private void updateUserInfo(UserEntity user, UserEntity updates) {
        if (updates.getEmail() != null) user.setEmail(updates.getEmail());
        if (updates.getFirstName() != null) user.setFirstName(updates.getFirstName());
        if (updates.getLastName() != null) user.setLastName(updates.getLastName());
        if (updates.getBirthDate() != null) user.setBirthDate(updates.getBirthDate());
        if (updates.getAddress() != null) user.setAddress(updates.getAddress());
        if (updates.getPhoneNumber() != null) user.setPhoneNumber(updates.getPhoneNumber());
    }

}
