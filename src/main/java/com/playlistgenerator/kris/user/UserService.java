package com.playlistgenerator.kris.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public UserEntity createUser(UserEntity userEntity) {
        String hashedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(hashedPassword);
        return userRepository.save(userEntity);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean doesUserExistsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean doesUserExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public int enableUser(Long id) {
        return userRepository.updateEnableStatusById(id, true);
    }

    @Transactional
    public int updatePassword(UserEntity user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        return userRepository.updatePasswordById(user.getUserId(), hashedPassword);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsername(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User with username: \"" + username + "\" is not found.");

        return new UserPrinciple(user.get());
    }
}
