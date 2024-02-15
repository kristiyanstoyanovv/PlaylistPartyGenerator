package com.playlistgenerator.kris.user;

import com.playlistgenerator.kris.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET u.isEnabled = ?2 WHERE u.userId=?1")
    int updateEnableStatusById(Long userId, Boolean status);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET u.password = ?2 WHERE u.userId=?1")
    int updatePasswordById(Long userId, String password);
}
