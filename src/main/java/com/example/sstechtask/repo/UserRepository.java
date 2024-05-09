package com.example.sstechtask.repo;

import com.example.sstechtask.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAllByBirthDateBetween(Date startDate, Date endDate);

}
